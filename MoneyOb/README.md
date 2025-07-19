# ObTracker

Un mod Minecraft Fabric pour tracker et afficher vos statistiques de jeu en temps réel.

## 🎯 Fonctionnalités

### 📊 Suivi des Statistiques
- **Minions** : Gains d'argent et nombre d'objets collectés
- **Niveau d'île** : Progression et gains par minute
- **Item** : Affichage de l'item actuellement équipé

### ⚙️ Configuration
- Interface de configuration accessible via une touche de raccourci
- Activation/désactivation individuelle de chaque catégorie
- Boutons de reset pour chaque catégorie

## 🎮 Utilisation

### Contrôles
- **U** : Ouvrir le menu de configuration
- **Échap** ou **/** : Fermer le menu de configuration

### Menu de Configuration
Le menu permet de :
- Activer/désactiver l'affichage de chaque catégorie
- Reset des statistiques pour les Minions (bouton R)
- Reset des statistiques pour le Niveau d'île (bouton R)

## 🔧 Installation

1. Assurez-vous d'avoir **Fabric Loader** installé
2. Placez le fichier `.jar` du mod dans votre dossier `mods/`
3. Lancez Minecraft

## 📋 Prérequis

- **Minecraft** : 1.21.4
- **Fabric Loader** : 0.16.14+
- **Fabric API** : Requis

## 🏗️ Développement

### Compilation
```bash
./gradlew build
```

### Test en développement
```bash
./gradlew runClient
```

## 📝 Structure du Projet

- `src/main/java/com/noirtrou/obtracker/` : Code source principal
  - `gui/` : Interface utilisateur et configuration
  - `listeners/` : Écouteurs d'événements
  - `mixin/` : Mixins pour l'intégration Minecraft
  - `tracker/` : Logique de suivi des données
  - `utils/` : Utilitaires

## 🐛 Problèmes Connus

Aucun problème connu pour le moment. Signalez les bugs via les issues GitHub.

## 📄 Licence

Projet personnel - Tous droits réservés.
