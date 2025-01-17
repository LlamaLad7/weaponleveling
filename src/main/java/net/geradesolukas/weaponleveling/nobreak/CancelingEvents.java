package net.geradesolukas.weaponleveling.nobreak;

import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CancelingEvents {

    public static boolean isBroken (ItemStack stack) {
        return stack.getDamageValue() >= (stack.getMaxDamage() + 1);
    }

    //@OnlyIn(Dist.CLIENT)
    //@SubscribeEvent
    //public static void onRenderHand(RenderHandEvent event) {
    //    ItemStack stack = event.getItemStack();
    //    if (ItemUtils.isBroken(stack)) {
    //        event.setCanceled(true);
    //    }
    //}

    @SubscribeEvent
    public static void onArmor(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof ArmorItem) {
            if (ItemUtils.isBroken(event.getItemStack())) {
                Attribute armor = Attributes.ARMOR;
                Attribute armorToughness = Attributes.ARMOR_TOUGHNESS;
                Attribute knockbackResistanceesistance = Attributes.KNOCKBACK_RESISTANCE;
                removeAttributeModifer(event,armor);
                removeAttributeModifer(event,armorToughness);
                removeAttributeModifer(event,knockbackResistanceesistance);
            }
        }
    }

    public static void removeAttributeModifer(ItemAttributeModifierEvent event, Attribute attribute) {
        if (event.getModifiers().get(attribute).stream().findFirst().isPresent()) {
            AttributeModifier modifier = event.getModifiers().get(attribute).stream().findFirst().get();
            event.removeModifier(attribute,modifier);
        }
    }

    @SubscribeEvent
    public static void onItemDestroy(PlayerDestroyItemEvent event) {
        if (!event.getPlayer().isCreative() && WeaponLevelingConfig.Server.broken_items_wont_vanish.get() && event.getOriginal().isDamageableItem() && event.getOriginal().getDamageValue() >= (event.getOriginal().getMaxDamage())) {
            ItemStack stack = event.getOriginal().copy();
            stack.setDamageValue(stack.getMaxDamage() + 1);
            if (event.getHand() != null) {
                event.getPlayer().setItemInHand(event.getHand(), stack);
                //event.getPlayer().sendMessage(new TextComponent("This is the item" + stack),event.getPlayer().getUUID());
            }
        }
    }
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!event.getPlayer().isCreative() && isBroken(event.getPlayer().getMainHandItem())) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (!event.getPlayer().isCreative() && isBroken(event.getBow())) {
            event.setCanceled(true);
        }

    }
    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative() && isBroken(event.getPlayer().getMainHandItem())) {
            event.setCanceled(true);
        }

    }
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!event.getPlayer().isCreative() && isBroken(event.getPlayer().getMainHandItem())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void onUseItem(LivingEntityUseItemEvent.Start event) {
        if(event.getEntityLiving() instanceof Player player) {
            if (!player.isCreative() && ItemUtils.isBroken(event.getItem())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onBlockModify(BlockEvent.BlockToolModificationEvent event) {
        if( event.getPlayer() != null) {
            if (!event.getPlayer().isCreative() && isBroken(event.getPlayer().getMainHandItem())) {
                event.setCanceled(true);
            }
        }
    }
}
