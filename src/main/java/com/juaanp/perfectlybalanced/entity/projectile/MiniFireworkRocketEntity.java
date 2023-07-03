package com.juaanp.perfectlybalanced.entity.projectile;

import com.juaanp.perfectlybalanced.PerfectlyBalanced;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;

public class MiniFireworkRocketEntity extends FireworkRocketEntity {

    private static final TrackedData<ItemStack> ITEM;
    private static final TrackedData<OptionalInt> SHOOTER_ENTITY_ID;
    private static final TrackedData<Boolean> SHOT_AT_ANGLE;
    private int life;
    private int lifeTime;
    @Nullable
    private LivingEntity shooter;

    private Logger logger = PerfectlyBalanced.LOGGER;

    public MiniFireworkRocketEntity(EntityType<? extends FireworkRocketEntity> entityType, World world) {
        super(entityType, world);
    }

    public MiniFireworkRocketEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityType.FIREWORK_ROCKET, world);
        this.life = 100;
        this.setPosition(x, y, z);
        int i = 1;
        if (!stack.isEmpty() && stack.hasNbt()) {
            this.dataTracker.set(ITEM, stack.copy());
        }

        this.setVelocity(this.random.nextTriangular(0.0, 0.002297), 0.05, this.random.nextTriangular(0.0, 0.002297));
        this.lifeTime = 100 * i + this.random.nextInt(6) + this.random.nextInt(7);
        logger.info("ROCKET");
    }

    public MiniFireworkRocketEntity(World world, @Nullable Entity entity, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z, stack);
        this.setOwner(entity);
    }

    public MiniFireworkRocketEntity(World world, ItemStack stack, LivingEntity shooter) {
        this(world, shooter, shooter.getX(), shooter.getY(), shooter.getZ(), stack);
        this.dataTracker.set(SHOOTER_ENTITY_ID, OptionalInt.of(shooter.getId()));
        this.shooter = shooter;
    }

    public MiniFireworkRocketEntity(World world, ItemStack stack, double x, double y, double z, boolean shotAtAngle) {
        this(world, x, y, z, stack);
        this.dataTracker.set(SHOT_AT_ANGLE, shotAtAngle);
    }

    public MiniFireworkRocketEntity(World world, ItemStack stack, Entity entity, double x, double y, double z, boolean shotAtAngle) {
        this(world, stack, x, y, z, shotAtAngle);
        this.setOwner(entity);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ITEM, ItemStack.EMPTY);
    }

    public boolean shouldRender(double distance) {
        return distance < 4096.0 && !this.wasShotByEntity();
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return super.shouldRender(cameraX, cameraY, cameraZ) && !this.wasShotByEntity();
    }

    public void tick() {
        super.tick();
        Vec3d vec3d3;
        if (this.wasShotByEntity()) {
            if (this.shooter == null) {
                ((OptionalInt)this.dataTracker.get(SHOOTER_ENTITY_ID)).ifPresent((id) -> {
                    Entity entity = this.world.getEntityById(id);
                    if (entity instanceof LivingEntity) {
                        this.shooter = (LivingEntity)entity;
                    }

                });
            }

            if (this.shooter != null) {
                if (this.shooter.isFallFlying()) {
                    Vec3d vec3d = this.shooter.getRotationVector();
                    double d = 1.5;
                    double e = 0.1;
                    Vec3d vec3d2 = this.shooter.getVelocity();
                    this.shooter.setVelocity(vec3d2.add(vec3d.x * 0.1 + (vec3d.x * 1.5 - vec3d2.x) * 0.5, vec3d.y * 0.1 + (vec3d.y * 1.5 - vec3d2.y) * 0.5, vec3d.z * 0.1 + (vec3d.z * 1.5 - vec3d2.z) * 0.5));
                    vec3d3 = this.shooter.getHandPosOffset(Items.FIREWORK_ROCKET);
                } else {
                    vec3d3 = Vec3d.ZERO;
                }

                this.setPosition(this.shooter.getX() + vec3d3.x, this.shooter.getY() + vec3d3.y, this.shooter.getZ() + vec3d3.z);
                this.setVelocity(this.shooter.getVelocity());
            }
        } else {
            if (!this.wasShotAtAngle()) {
                double f = this.horizontalCollision ? 1.0 : 1.15;
                this.setVelocity(this.getVelocity().multiply(f, 1.0, f).add(0.0, 0.04, 0.0));
            }

            vec3d3 = this.getVelocity();
            this.move(MovementType.SELF, vec3d3);
            this.setVelocity(vec3d3);
        }

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (!this.noClip) {
            this.onCollision(hitResult);
            this.velocityDirty = true;
        }

        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            this.world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.life;
        if (this.world.isClient && this.life % 2 < 2) {
            this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
        }

        if (!this.world.isClient && this.life > this.lifeTime) {
            this.explodeAndRemove();
        }

    }

    private void explodeAndRemove() {
        this.world.sendEntityStatus(this, EntityStatuses.EXPLODE_FIREWORK_CLIENT);
        this.emitGameEvent(GameEvent.EXPLODE, this.getOwner());
        this.explode();
        this.discard();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            this.explodeAndRemove();
        }
    }

    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos blockPos = new BlockPos(blockHitResult.getBlockPos());
        this.world.getBlockState(blockPos).onEntityCollision(this.world, blockPos, this);
        if (!this.world.isClient() && this.hasExplosionEffects()) {
            this.explodeAndRemove();
        }

        super.onBlockHit(blockHitResult);
    }

    private boolean hasExplosionEffects() {
        ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
        NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
        NbtList nbtList = nbtCompound != null ? nbtCompound.getList("Explosions", NbtElement.COMPOUND_TYPE) : null;
        return nbtList != null && !nbtList.isEmpty();
    }

    private void explode() {
        float f = 0.0F;
        ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
        NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
        NbtList nbtList = nbtCompound != null ? nbtCompound.getList("Explosions", NbtElement.COMPOUND_TYPE) : null;
        if (nbtList != null && !nbtList.isEmpty()) {
            f = 5.0F + (float)(nbtList.size() * 2);
        }

        if (f > 0.0F) {
            if (this.shooter != null) {
                this.shooter.damage(DamageSource.firework(this, this.getOwner()), 5.0F + (float)(nbtList.size() * 2));
            }

            double d = 5.0;
            Vec3d vec3d = this.getPos();
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(5.0));
            Iterator var9 = list.iterator();

            while(true) {
                LivingEntity livingEntity;
                do {
                    do {
                        if (!var9.hasNext()) {
                            return;
                        }

                        livingEntity = (LivingEntity)var9.next();
                    } while(livingEntity == this.shooter);
                } while(this.squaredDistanceTo(livingEntity) > 25.0);

                boolean bl = false;

                for(int i = 0; i < 2; ++i) {
                    Vec3d vec3d2 = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5 * (double)i), livingEntity.getZ());
                    HitResult hitResult = this.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                    if (hitResult.getType() == HitResult.Type.MISS) {
                        bl = true;
                        break;
                    }
                }

                if (bl) {
                    float g = f * (float)Math.sqrt((5.0 - (double)this.distanceTo(livingEntity)) / 5.0);
                    livingEntity.damage(DamageSource.firework(this, this.getOwner()), g);
                }
            }
        }
    }

    private boolean wasShotByEntity() {
        return ((OptionalInt)this.dataTracker.get(SHOOTER_ENTITY_ID)).isPresent();
    }

    public boolean wasShotAtAngle() {
        return (Boolean)this.dataTracker.get(SHOT_AT_ANGLE);
    }

    public void handleStatus(byte status) {
        if (status == EntityStatuses.EXPLODE_FIREWORK_CLIENT && this.world.isClient) {
            if (!this.hasExplosionEffects()) {
                for(int i = 0; i < this.random.nextInt(3) + 2; ++i) {
                    this.world.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
                }
            } else {
                ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
                NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
                Vec3d vec3d = this.getVelocity();
                this.world.addFireworkParticle(this.getX(), this.getY(), this.getZ(), vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            }
        }

        super.handleStatus(status);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Life", this.life);
        nbt.putInt("LifeTime", this.lifeTime);
        ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
        if (!itemStack.isEmpty()) {
            nbt.put("FireworksItem", itemStack.writeNbt(new NbtCompound()));
        }

        nbt.putBoolean("ShotAtAngle", (Boolean)this.dataTracker.get(SHOT_AT_ANGLE));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.life = nbt.getInt("Life");
        this.lifeTime = nbt.getInt("LifeTime");
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("FireworksItem"));
        if (!itemStack.isEmpty()) {
            this.dataTracker.set(ITEM, itemStack);
        }

        if (nbt.contains("ShotAtAngle")) {
            this.dataTracker.set(SHOT_AT_ANGLE, nbt.getBoolean("ShotAtAngle"));
        }

    }

    public ItemStack getStack() {
        ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
        return itemStack.isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : itemStack;
    }

    public boolean isAttackable() {
        return false;
    }

    static {
        ITEM = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        SHOOTER_ENTITY_ID = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.OPTIONAL_INT);
        SHOT_AT_ANGLE = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}