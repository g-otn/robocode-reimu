# robocode-reimu
A simple Robocode MegaBot AdvancedRobot for meelee and 1v1. 

It was created by the "Alpha" team for the [6th Robotica Paula Souza's Robocode championship](http://www.robotica.cpscetec.com.br/robocode.php) (2018) and finished in 2nd place.
[Check out its battles](https://goo.gl/bB79ZQ) (look for the videos with "Fatec108" on the title).

The name "Reimu" is a reference of the main character of the Touhou Project game series Reimu, who dodges bullets in the game.

***Veja este README.md em [Português brasileiro](https://github.com/g-otn/robocode-reimu/blob/master/README.pt-BR.md).***

## Main strategy
While doing a spiral-like movement towards the target, it monitors the target's energy to check if it shot. 
If a shot is detected, it changes the rotation direction. This dodges most robots with Head-On and Linear targeting.

<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59645892-a4ccbf80-914a-11e9-902a-5db055b0a4b3.gif">
</p>

## Other Features
- One on One Radar
- Linear Targeting (slightly modified for small turns for small distances)
- Target change based on being hit, bullet hit, distance and health
- Bullet power change based on distance and own health
- Wall anti-collision (broken)

<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59646847-11e25400-914f-11e9-82ce-d75b3dc52f4d.gif">
</p>

Demo of Reimu changing targets by choosing lowest health when acidentally ramming and when shot.

## Download .jar
See [Releases](https://github.com/g-otn/robocode-reimu/releases).
