package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public abstract class MixinCrossbowItem {

    @Inject(
            method = "getArrow",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setShotFromCrossbow(Z)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(Level p_40915_, LivingEntity p_40916_, ItemStack stack, ItemStack p_40918_, CallbackInfoReturnable<AbstractArrow> cir, ArrowItem arrowitem, AbstractArrow abstractarrow) {
        if(ItemUtils.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(stack) * ItemUtils.getBowlikeModifier(stack);
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }


}
