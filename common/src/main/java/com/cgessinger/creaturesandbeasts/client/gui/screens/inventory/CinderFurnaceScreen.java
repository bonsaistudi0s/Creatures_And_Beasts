package com.cgessinger.creaturesandbeasts.client.gui.screens.inventory;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.menus.CinderFurnaceMenu;
import com.mojang.blaze3d.systems.RenderSystem;
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
import org.jetbrains.annotations.NotNull;

public class CinderFurnaceScreen extends AbstractContainerScreen<CinderFurnaceMenu> implements RecipeUpdateListener {
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "textures/gui/container/cinder_furnace.png");;

    public CinderFurnaceScreen(CinderFurnaceMenu cinderFurnaceMenu, Inventory inventory, Component component) {
        super(cinderFurnaceMenu, inventory, component);
        this.recipeBookComponent = new SmeltingRecipeBookComponent();
    }

    @Override
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
        guiGraphics.blit(this.texture, i, j, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(this.texture, i + 57, j + 37, 176, 0, 14, 14);
        int l = this.menu.getCookingProgress();
        guiGraphics.blit(this.texture, i + 79, j + 35, 176, 14, l + 1, 16);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, partialTick);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        boolean bl = mouseX < (double)guiLeft
                || mouseY < (double)guiTop
                || mouseX >= (double)(guiLeft + this.imageWidth)
                || mouseY >= (double)(guiTop + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && bl;
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public @NotNull RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
