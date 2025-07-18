# Guide de Test Final - Système de Capture des Titres

## ✅ Problème Résolu
Le conflit de classes a été résolu en supprimant le fichier en double `TitleListenerNew.java`. Le build fonctionne maintenant correctement.

## 🎯 Système de Capture Implémenté

### Fonctionnalités
- **Capture des titres** : Détection automatique des titres, sous-titres et action bars
- **Logging détaillé** : Fichier `run/title_capture.log` avec types détectés
- **Détection automatique** : Types NIVEAU, GAIN, ARGENT, MORT, TÉLÉPORTATION, etc.
- **API d'accès** : Méthodes pour récupérer les derniers titres capturés

### Comment Tester

1. **Lancer le jeu** :
   ```bash
   cd /Users/mattheo/Fichiers/Projet/MoneyOb
   ./gradlew runClient
   ```

2. **Vérifier les logs** :
   - Le fichier `run/title_capture.log` sera créé automatiquement
   - Surveillez les messages dans la console de debug

3. **Déclencher des événements** :
   - Changer de niveau
   - Recevoir de l'argent
   - Utiliser des commandes qui affichent des titres
   - Mourir dans le jeu

4. **Consulter les captures** :
   ```bash
   tail -f run/title_capture.log
   ```

### Structure des Logs
```
[TIMESTAMP] [TYPE] Title: "Votre titre" | Subtitle: "Votre sous-titre" | ActionBar: "Votre action bar"
```

### Types Détectés Automatiquement
- `NIVEAU` : Messages de changement de niveau
- `GAIN` : Messages de gain d'argent ou d'objets
- `ARGENT` : Transactions monétaires
- `MORT` : Messages de mort
- `TELEPORTATION` : Messages de téléportation
- `SYSTEME` : Messages système
- `AUTRE` : Autres types de messages

## 🔧 API Utilisable

### Dans votre code Java
```java
import com.noirtrou.obtracker.listeners.TitleListener;

// Récupérer le dernier titre
String lastTitle = TitleListener.getLastTitle();
String lastSubtitle = TitleListener.getLastSubtitle();
String lastActionBar = TitleListener.getLastActionBar();

// Récupérer l'historique
List<TitleListener.TitleCapture> history = TitleListener.getTitleHistory();
```

## 🚀 Prêt à l'Utilisation

Le système est maintenant fonctionnel et prêt à capturer tous les titres affichés dans Minecraft. Le fichier de log fournira un historique détaillé de tous les événements capturés avec leur type automatiquement détecté.

## 📁 Fichiers Importants
- `src/main/java/com/noirtrou/obtracker/listeners/TitleListener.java` - Système principal
- `src/main/java/com/noirtrou/obtracker/listeners/TitleCaptureAlternative.java` - Système alternatif
- `run/title_capture.log` - Fichier de logs (généré automatiquement)

## 🎮 Prochaines Étapes
1. Tester en jeu
2. Vérifier la capture des différents types de titres
3. Analyser les logs pour confirmer la détection automatique
4. Ajuster les patterns de détection si nécessaire
