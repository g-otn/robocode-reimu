# robocode-reimu
A simple Robocode MegaBot AdvancedRobot for meelee and 1v1. 
It was created by the "Alpha" team for the [6th Robotica Paula Souza's Robocode championship](http://www.robotica.cpscetec.com.br/robocode.php) (2018) and finished in 2nd place.

*Um simples Robocode MegaBot AdvancedRobot para meelee e 1v1. 
Foi criado pelo o time "Alpha" para o [6º campeonato de Robocode do Robotica Paula Souza](http://www.robotica.cpscetec.com.br/robocode.php) (2018) e terminou em 2º lugar.*

The name "Reimu" is a reference of the main character of the Touhou Project game series Reimu, which dodges bullets in the game.

*O nome "Reimu" é uma referência a personagem principal Reimu, da série de jogos Touhou Project, a qual desvia de projéteis no jogo.*

## Main strategy
While doing a spiral-like movement towards the target, it monitors the target's energy to check if it shot. 
If a shot is detected, it changes the rotation direction. This dodges most robots with Head-On and Linear targeting.

*Enquanto faz um movimento em espiral em direção ao alvo, ele monitora a energia para checar se o alvo atirou.
Se um tiro foi detectado, ele troca o sentido de rotação. Isso desvia da maioria dos robôs que alvejam com Head-On ou Linear.*

<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59645892-a4ccbf80-914a-11e9-902a-5db055b0a4b3.gif">
</p>

## Other Features (*Outras Características*)
- One on One Radar
- Linear Targeting (slightly modified for small turns for small distances)
- Target change based on being hit, bullet hit, distance and health
- Bullet power change based on distance and own health
- Wall anti-collision (broken)

<br>

- *Radar One on One*
- *Linear targeting (levemente modificado para pequenas voltas para pequenas distâncias)*
- *Troca de alvo baseado em ser atingido, acerto de projétil, distância e vida*
- *Troca de potência de projétil baseado em distância e na própria vida*
- *Anticolisão com a parede (quebrado)*


<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59646847-11e25400-914f-11e9-82ce-d75b3dc52f4d.gif">
</p>

Demo of Reimu changing targets by choosing lowest health when acidentally ramming and when shot.

*Demonstração da Reimu trocando de alvo selecionando a menor vida ao acidentalmente "esbarrar" e quando atingida.*

## Download .jar
See [Releases](https://github.com/g-otn/robocode-reimu/releases). *Veja [Releases](https://github.com/g-otn/robocode-reimu/releases).*
