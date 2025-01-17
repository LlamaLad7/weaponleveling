package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class MixinPlayer {


    @Inject(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Entity target, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack stack, Entity entity) {
        if(ItemUtils.isAcceptedMeleeWeaponStack(stack)) {
            var player = ((Player) ((Object)this) );
            UpdateLevels.applyXPOnItemStack(stack,  player, target, flag2);
            if (flag2) {UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForCrit(stack));}

        }
    }


    //@ModifyArg(
    //        method = "attack",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"),index = 1)
    //private ItemStack replaceEmpty(ItemStack emptystack) {
    //    ItemStack stack = ((Player) ((Object)this) ).getMainHandItem();
    //    stack.setDamageValue(stack.getMaxDamage() + 1);
    //    return stack;
    //}

    @WrapOperation(
            method = "Lnet/minecraft/world/entity/player/Player;hurtCurrentlyUsedShield(F)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private void wrapReplaceShield1(Player instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original, float pDamage) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getItemBySlot(slot);
            stack.setDamageValue(stack.getMaxDamage() + 1);
            instance.setItemSlot(slot, stack);
        }
    }

    @WrapOperation(
            method = "Lnet/minecraft/world/entity/player/Player;hurtCurrentlyUsedShield(F)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
    private void wrapReplaceShield2(Player instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original, float pDamage) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getItemBySlot(slot);
            stack.setDamageValue(stack.getMaxDamage() + 1);
            instance.setItemSlot(slot, stack);
        }
    }

}
