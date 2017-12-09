# Galactic Foundations
<p align="center">
     <img width="678" height="162"
          title="Galactic Foundations Logo" src="./android/assets/Title.png"> 
</p>
     
## What is it?
Galactic Foundations is a mobile strategy game made using the libGDX library.
This game was developed primarily for use on Android, however a desktop version is available.

## Details
This was a term project for an undergraduate level Software Engineering course.
The gameplay concept for Galactic Foundations was designed by the team from scratch,
prioritizing strategy and simplicity above other factors.

### Purpose ###
The purpose of this project was to understand and implement the stages of conventional software development.

### Process ###
This project was developed using the Waterfall method and consisted of four main stages:

 - Design a simple game for Android mobile devices.  
 - Software Requirements Analysis    
 - Test Cases  
 - Implementation  

## How To Play ##
Galactic Foundations is a hexagon based strategy game where you and an opponent take turns spending points to gain territory.
Your territory is green, while your opponents territory is red.

#### Objective ####
Destroy the opponent's base by expanding your territory within range and attacking your opponent.
You lose if your opponent destroys your base first.

#### Types of Hexes ####
There are three types of hexagons you can control:
 - Base Hexagon, worth 5 points
 - Special Resource Hexagons, worth 3 points each
 - General Hexagons, worth 2 points each

#### Points System ####
At the beginnning of each player's turn, the player will have all hexagons with a connection to their base "reactivated", meaning the hexagon
will light up, and its value will be added to your points total.

#### Actions ####
Each hexagon can be used for one action each turn, after which it will "deactivate" and go dark.
 - Expansion:  
 The player can select a hexagon and select the "EXP" button, highlighting all hexagons that can be expanded to.
 Only unowned tiles can be expaneded to. Expanding costs the player the amount that the hexagon is worth, so Special Resource hexagons
 would cost 3 points to expand to.  
 - Fortification:  
 The player may choose to select the "DEF" button and fortify any hexagon that is not a Base hexagon. Fortification costs two points and will protect the hexagon from
 one enemy attack. If a fortified tile is attacked, a ray attack will not pass on to the next hexagon.
 - Attack:  
 Only when a hexagon is adjacent to enemy territory, the player may select the "ATT" button and spend five points to perform a ray attack on the selected enemy hexgon.
 A ray attack will remove enemy control of up to three hexagons in a line of the direction of the attack. If the attack reaches a fortified hexagon,
 the attack will be stopped there.  
 - Stockpile:  
 The "STO" option stockpiles the player's points and begins the opponents turn.  
 
## Check it out\! ##  
[Android](https://github.com/lauesa/GalacticFoundations/raw/master/Binder/Galactic%20Foundations.apk)  
[Desktop](https://github.com/lauesa/GalacticFoundations/raw/master/Binder/Galactic%20Foundations.jar)  
*Note:* Galactic Foundations.jar will create a text file named "gamestate.txt", this contains save data.
## Authors ##
 - [Scott Laue](https://github.com/lauesa)  
 - [Warren Smith](https://github.com/Dubyahs)  
 - [Jake Nissely](https://github.com/jakenissley)
 - [Kevin Williams)(https://github.com/Borzantag)
