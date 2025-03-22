package com.cgessinger.creaturesandbeasts.client.gui.screens.inventory;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsCommon;
import com.cgessinger.creaturesandbeasts.menus.CinderFurnaceMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class CinderFurnaceScreen extends AbstractContainerScreen<CinderFurnaceMenu> implements RecipeUpdateListener {
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsCommon.MOD_ID, "textures/gui/container/cinder_furnace.png");;

    public CinderFurnaceScreen(CinderFurnaceMenu cinderFurnaceMenu, Inventory inventory, Component component) {
        super(cinderFurnaceMenu, inventory, component);
        this.recipeBookComponent = new SmeltingRecipeBookComponent();
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(poseStack, i + 57, j + 37, 176, 0, 14, 14);
        int l = this.menu.getCookingProgress();
        this.blit(poseStack, i + 79, j + 35, 176, 14, l + 1, 16);
    }

    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    public void render(PoseStack poseStack, int p_97859_, int p_97860_, float p_97861_) {
        this.renderBackground(poseStack);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(poseStack, p_97861_, p_97859_, p_97860_);
            this.recipeBookComponent.render(poseStack, p_97859_, p_97860_, p_97861_);
        } else {
            this.recipeBookComponent.render(poseStack, p_97859_, p_97860_, p_97861_);
            super.render(poseStack, p_97859_, p_97860_, p_97861_);
            this.recipeBookComponent.renderGhostRecipe(poseStack, this.leftPos, this.topPos, true, p_97861_);
        }

        this.renderTooltip(poseStack, p_97859_, p_97860_);
        this.recipeBookComponent.renderTooltip(poseStack, this.leftPos, this.topPos, p_97859_, p_97860_);
    }

    public boolean mouseClicked(double x, double y, int z) {
        if (this.recipeBookComponent.mouseClicked(x, y, z)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(x, y, z);
        }
    }

    protected void slotClicked(Slot slot, int p_97849_, int p_97850_, ClickType clickType) {
        super.slotClicked(slot, p_97849_, p_97850_, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    public boolean keyPressed(int p_97844_, int p_97845_, int p_97846_) {
        return !this.recipeBookComponent.keyPressed(p_97844_, p_97845_, p_97846_) && super.keyPressed(p_97844_, p_97845_, p_97846_);
    }

    protected boolean hasClickedOutside(double p_97838_, double p_97839_, int p_97840_, int p_97841_, int p_97842_) {
        boolean flag = p_97838_ < (double)p_97840_ || p_97839_ < (double)p_97841_ || p_97838_ >= (double)(p_97840_ + this.imageWidth) || p_97839_ >= (double)(p_97841_ + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(p_97838_, p_97839_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_97842_) && flag;
    }

    public boolean charTyped(char p_97831_, int p_97832_) {
        return this.recipeBookComponent.charTyped(p_97831_, p_97832_) || super.charTyped(p_97831_, p_97832_);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    public void removed() {
        this.recipeBookComponent.removed();
        super.removed();
    }
}
