package com.galaxyhells.skylake.features.hud.itemlog;

/**
 * Representa uma entrada no log de itens coletados
 */
public class ItemLogEntry {
    private final String itemName;
    private final int quantity;
    private final long timestamp;
    private float displayY;
    private float alpha;

    public ItemLogEntry(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
        this.displayY = 0;
        this.alpha = 1.0F;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getDisplayY() {
        return displayY;
    }

    public void setDisplayY(float displayY) {
        this.displayY = displayY;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * Retorna o texto formatado para exibição
     */
    public String getDisplayText() {
        return "§a+ " + quantity + "x §f" + itemName;
    }

    /**
     * Verifica se a entrada ainda deve ser exibida (3 segundos de vida)
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > 3000;
    }
}
