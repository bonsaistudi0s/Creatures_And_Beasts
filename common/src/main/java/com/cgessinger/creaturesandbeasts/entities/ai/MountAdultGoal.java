package com.cgessinger.creaturesandbeasts.entities.ai;

import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

import java.util.List;

public class MountAdultGoal extends Goal {
    private final Animal childAnimal;
    private final double moveSpeed;

    public MountAdultGoal(Animal child, double speed) {
        this.childAnimal = child;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        if (!this.childAnimal.isPassenger() && this.childAnimal.isBaby()) {
            List<LittleGrebeEntity> entities = this.childAnimal.level().getEntitiesOfClass(LittleGrebeEntity.class, this.childAnimal.getBoundingBox().inflate(10, 3, 10));

            for (LittleGrebeEntity entity : entities) {
                if (!entity.isBaby() && !entity.isVehicle()) {
                    this.childAnimal.getNavigation().moveTo(this.childAnimal.getNavigation().createPath(entity, 0), this.moveSpeed);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        List<LittleGrebeEntity> list = this.childAnimal.level().getEntitiesOfClass(LittleGrebeEntity.class, this.childAnimal.getBoundingBox());

        for (LittleGrebeEntity grebe : list) {
            if (!grebe.equals(this.childAnimal) && !grebe.isBaby() && !grebe.isVehicle()) {
                this.childAnimal.startRiding(grebe);
            }
        }
    }
}
