# Système de Capture de Titres Minecraft

Ce système permet de capturer et analyser tous les titres, sous-titres et messages d'action bar qui apparaissent dans Minecraft via la commande `/title`.

## Fonctionnalités

### TitleListener
- **Capture automatique** : Intercepte tous les titres affichés via `/title`
- **Stockage** : Garde un historique des 100 derniers titres de chaque type
- **Analyse automatique** : Analyse les titres en temps réel pour détecter des patterns

### TitleAnalyzer
- **Analyse de patterns** : Détecte automatiquement les level-ups, achievements, etc.
- **Recherche** : Permet de rechercher des titres contenant un texte spécifique
- **Statistiques** : Génère des statistiques sur les titres capturés

## Utilisation

### Accès aux titres capturés
```java
// Récupérer tous les titres
List<String> titles = TitleListener.getCapturedTitles();

// Récupérer le dernier titre
String lastTitle = TitleListener.getLastTitle();

// Récupérer les sous-titres
List<String> subtitles = TitleListener.getCapturedSubtitles();

// Récupérer les messages d'action bar
List<String> actionBars = TitleListener.getCapturedActionBars();
```

### Analyse des titres
```java
// Analyser le dernier titre reçu
TitleAnalyzer.analyzeLastTitle();

// Analyser tous les titres pour des statistiques
TitleAnalyzer.analyzeAllTitles();

// Rechercher des titres contenant un mot-clé
List<String> results = TitleAnalyzer.searchTitles("niveau");

// Afficher les 5 derniers titres
TitleAnalyzer.printRecentTitles(5);
```

### Touches de raccourci (en jeu)
- **Touche T** : Affiche les 5 derniers titres capturés et les statistiques
- **Touche U** : Ouvre la configuration du mod (existant)

## Structure du code

### Fichiers créés/modifiés :
1. `TitleListener.java` - Gestionnaire principal de capture des titres
2. `InGameHudMixin.java` - Mixin pour intercepter les titres depuis l'InGameHud
3. `TitleAnalyzer.java` - Utilitaires d'analyse des titres
4. `KeyListener.java` - Ajout de la touche T pour les tests
5. `obtracker.mixins.json` - Configuration des mixins
6. `ObTrackerMod.java` - Enregistrement du TitleListener

## Personnalisation

Vous pouvez facilement personnaliser l'analyse des titres en modifiant :

1. **Patterns de détection** dans `TitleAnalyzer.java`
2. **Logique d'analyse automatique** dans `TitleListener.analyzeTitle()`
3. **Taille de l'historique** (actuellement 100 titres)

## Exemples d'utilisation

### Détecter des gains d'expérience
```java
private static final Pattern XP_PATTERN = Pattern.compile("\\+(\\d+) XP");

// Dans analyzeTitle()
Matcher xpMatcher = XP_PATTERN.matcher(titleText);
if (xpMatcher.find()) {
    int xp = Integer.parseInt(xpMatcher.group(1));
    // Traquer les gains d'XP
}
```

### Détecter des événements spéciaux
```java
if (titleText.toLowerCase().contains("événement")) {
    // Logique pour les événements
}
```

Ce système est conçu pour être extensible et s'adapter facilement aux besoins spécifiques de votre serveur Minecraft.
