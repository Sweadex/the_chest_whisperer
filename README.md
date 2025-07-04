# 💰 The Chest Whisperer

_**Protégez vos coffres. Dénoncez les voleurs.**_

**The Chest Whisperer** est un mod *Minecraft* destiné aux serveurs multijoueurs, qui introduit un système intelligent de **surveillance et de protection des coffres**. Chaque coffre est protégé par un système de **propriété automatique** et de **détection de vol en temps réel**.

---

## 🔐 Fonctionnalités

* **Propriétaire automatique**: Le premier joueur à poser ou ouvrir un coffre en devient automatiquement le propriétaire.
* **Détection de vol**: Si un autre joueur ouvre le coffre et y retire des objets, il est immédiatement enregistré comme **voleur**.
* **Suivi du dernier voleur**: Chaque coffre garde en mémoire le **nom du dernier voleur** identifié.
* **Affichage visuel**: Les coffres volés changent d’apparence grâce à une **texture personnalisée** qui indique qu’un vol a eu lieu.
* **Support des double coffres**: Le système fonctionne parfaitement avec les **coffres doubles** (gauche/droite).
* **Commande d’administration**: `/checkchest position` — Permet aux opérateurs (OPs) de voir le **propriétaire** et le **dernier voleur** d’un coffre donné.

---

## 🔁 Réinitialisation automatique

Lorsque le **propriétaire** ouvre son propre coffre, les données de vol sont automatiquement effacées :

* Le champ `lastThief` est supprimé.
* Le coffre n’est plus marqué comme "volé" visuellement.

_*"Il rouvre discrètement son coffre, détruit toutes les preuves du vol, puis referme soigneusement, comme si de rien n'était."*_

Cela permet au joueur de **récupérer la possession de son coffre** sans intervention manuelle ni commande.
