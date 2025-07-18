# 🎯 Instructions de Test pour la Capture de Titres

## 📋 Système Amélioré

Le système a été complètement refondu avec :

### ✅ **Détection Multiple**
- **InGameHudMixin** : Intercepte les méthodes d'affichage de titre
- **TitlePacketMixin** : Intercepte les paquets réseau de titre
- **Logging détaillé** : Chaque détection est enregistrée

### ✅ **Fichier de Log Complet**
- **Emplacement** : `run/title_capture.log`
- **Contenu** : Timestamps, types détectés, contenu des titres
- **Format** : Horodatage + Type + Contenu

## 🎮 **Commandes de Test**

### **Test Basique**
```
/title @p title "Test Simple"
/title @p subtitle "Sous-titre Test"
/title @p actionbar "Action Bar Test"
```

### **Test avec Formatage**
```
/title @p title {"text":"Titre Formaté","color":"gold","bold":true}
/title @p subtitle {"text":"Sous-titre Coloré","color":"aqua"}
/title @p actionbar {"text":"Action Bar Stylée","color":"green"}
```

### **Test Réaliste pour Serveur**
```
/title @p title "Vous avez gagné 1,250.50$ (15x objets)"
/title @p subtitle "Félicitations !"
/title @p actionbar "§6💰 1,250$ §7| §c❤ 20/20 §7| §b⭐ Niveau 25"
```

## ⌨️ **Contrôles en Jeu**

- **T** : Afficher rapport de capture + statistiques
- **L** : Nettoyer l'historique et le log
- **U** : Ouvrir configuration (existant)

## 📊 **Vérifications**

### **1. Console de Jeu**
Recherchez ces messages :
```
[ObTracker] TITRE CAPTURÉ: ...
[ObTracker] SOUS-TITRE CAPTURÉ: ...
[ObTracker] ACTION BAR CAPTURÉE: ...
[ObTracker] DÉTECTION MÉTHODE: ...
```

### **2. Fichier de Log**
Vérifiez `run/title_capture.log` :
```
=== SESSION DÉMARRÉE À 2025-07-18 18:30:15.123 ===
[2025-07-18 18:30:20.456] DÉTECTION MÉTHODE: setTitle | Contenu: Test Simple
[2025-07-18 18:30:20.457] TITRE CAPTURÉ: Test Simple
  -> TYPES DÉTECTÉS: [selon le contenu]
```

### **3. Statistiques (Touche T)**
```
=== STATISTIQUES [timestamp] ===
Titres capturés: X
Sous-titres capturés: Y
Action bars capturées: Z
```

## 🔍 **Types Détectés Automatiquement**

### **Titres**
- `NIVEAU` : contient "niveau"
- `GAIN` : contient "gagné", "earn"
- `SUCCÈS` : contient "succès", "achievement"
- `ARGENT` : contient "$", "coin"
- `ÉVÉNEMENT` : contient "event", "événement"

### **Action Bars**
- `FINANCIER` : contient "$", "coin", "money", "argent"
- `COORDONNÉES` : contient "X:", "Y:", "Z:"
- `BARRE_PROGRESSION` : contient "❘", "|", "█"
- `POURCENTAGE` : contient "%"
- `VIE` : contient "hp", "vie"
- `MANA` : contient "mana", "mp"

## 🚀 **Test Complet**

1. **Compilez** le mod
2. **Lancez** Minecraft
3. **Exécutez** les commandes de test
4. **Appuyez sur T** pour voir les résultats
5. **Vérifiez** le fichier `run/title_capture.log`

## 🐛 **Dépannage**

Si rien n'est capturé :
1. Vérifiez que les mixins sont bien chargés
2. Regardez les erreurs dans la console
3. Vérifiez les permissions pour les commandes `/title`
4. Essayez différentes variantes de commandes

Le système capture maintenant **TOUT** et génère un **log détaillé** de chaque interaction !
