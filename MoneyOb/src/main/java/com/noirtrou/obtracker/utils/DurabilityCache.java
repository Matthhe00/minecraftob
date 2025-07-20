package com.noirtrou.obtracker.utils;

public class DurabilityCache {
    
    // Cache statique pour stocker la dernière durabilité trouvée
    private static String lastItemName = "";
    private static int lastDurability = -1;
    
    // Méthode pour stocker une durabilité capturée
    public static void storeDurability(String itemName, int durability) {
        lastItemName = itemName;
        lastDurability = durability;
    }
    
    // Méthode pour récupérer la durabilité capturée
    public static int getDurability(String itemName) {
        if (itemName != null && itemName.equals(lastItemName)) {
            return lastDurability;
        }
        return -1;
    }
    
    // Méthode pour vider le cache
    public static void clearCache() {
        lastItemName = "";
        lastDurability = -1;
    }
    
    // Méthode pour vérifier si un item a changé et nettoyer si nécessaire
    public static void checkAndClearIfItemChanged(String currentItemName) {
        if (!currentItemName.equals(lastItemName)) {
            clearCache();
        }
    }
}
