package com.galaxyhells.skylake.utils;

/**
 * Sistema de animações suaves com tweening
 */
public class AnimationUtils {
    
    /**
     * Função de easing linear
     */
    public static float linear(float t) {
        return t;
    }
    
    /**
     * Função de easing ease-in-out (suave)
     */
    public static float easeInOut(float t) {
        return t < 0.5f ? 2f * t * t : -1f + (4f - 2f * t) * t;
    }
    
    /**
     * Função de easing ease-out (desacelera suave)
     */
    public static float easeOut(float t) {
        return 1f - (1f - t) * (1f - t);
    }
    
    /**
     * Função de easing ease-in (acelera suave)
     */
    public static float easeIn(float t) {
        return t * t;
    }
    
    /**
     * Animação de fade com tweening
     */
    public static class FadeAnimation {
        private float startAlpha, endAlpha, currentAlpha;
        private long startTime, duration;
        
        public FadeAnimation(float startAlpha, float endAlpha, long duration) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
            this.duration = duration;
            this.currentAlpha = startAlpha;
            this.startTime = System.currentTimeMillis();
        }
        
        /**
         * Atualiza animação
         */
        public void update() {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(elapsed / (float)duration, 1.0f);
            currentAlpha = startAlpha + (endAlpha - startAlpha) * easeOut(progress);
        }
        
        /**
         * Obtém valor atual
         */
        public float getCurrent() {
            return currentAlpha;
        }
        
        /**
         * Verifica se animação terminou
         */
        public boolean isComplete() {
            return System.currentTimeMillis() - startTime >= duration;
        }
        
        /**
         * Reinicia animação
         */
        public void restart() {
            startTime = System.currentTimeMillis();
            currentAlpha = startAlpha;
        }
    }
    
    /**
     * Animação de slide com tweening
     */
    public static class SlideAnimation {
        private float startX, endX, currentX;
        private float startY, endY, currentY;
        private long startTime, duration;
        
        public SlideAnimation(float startX, float startY, float endX, float endY, long duration) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.duration = duration;
            this.currentX = startX;
            this.currentY = startY;
            this.startTime = System.currentTimeMillis();
        }
        
        public void update() {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(elapsed / (float)duration, 1.0f);
            float easedProgress = easeInOut(progress);
            
            currentX = startX + (endX - startX) * easedProgress;
            currentY = startY + (endY - startY) * easedProgress;
        }
        
        public float getCurrentX() {
            return currentX;
        }
        
        public float getCurrentY() {
            return currentY;
        }
        
        public boolean isComplete() {
            return System.currentTimeMillis() - startTime >= duration;
        }
        
        public void restart() {
            startTime = System.currentTimeMillis();
            currentX = startX;
            currentY = startY;
        }
    }
    
    /**
     * Animação de escala com tweening
     */
    public static class ScaleAnimation {
        private float startScale, endScale, currentScale;
        private long startTime, duration;
        
        public ScaleAnimation(float startScale, float endScale, long duration) {
            this.startScale = startScale;
            this.endScale = endScale;
            this.duration = duration;
            this.currentScale = startScale;
            this.startTime = System.currentTimeMillis();
        }
        
        public void update() {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(elapsed / (float)duration, 1.0f);
            currentScale = startScale + (endScale - startScale) * easeOut(progress);
        }
        
        public float getCurrent() {
            return currentScale;
        }
        
        public boolean isComplete() {
            return System.currentTimeMillis() - startTime >= duration;
        }
        
        public void restart() {
            startTime = System.currentTimeMillis();
            currentScale = startScale;
        }
    }
}
