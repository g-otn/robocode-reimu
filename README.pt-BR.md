# robocode-reimu
Um simples Robocode MegaBot AdvancedRobot para meelee e 1v1. 
Foi criado pelo o time "Alpha" para o [6º campeonato de Robocode do Robotica Paula Souza](http://www.robotica.cpscetec.com.br/robocode.php) (2018) e terminou em 2º lugar.

O nome "Reimu" é uma referência a personagem principal Reimu, da série de jogos Touhou Project, que se esquiva de projéteis no jogo.

## Estratégia
Enquanto faz um movimento em espiral em direção ao alvo, ele monitora a energia para checar se o alvo atirou.
Se um tiro foi detectado, ele troca o sentido de rotação. Isso desvia da maioria dos robôs que miram utilizando técnicas Head-On ou Linear.

<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59645892-a4ccbf80-914a-11e9-902a-5db055b0a4b3.gif">
</p>

## Outras Características
- Radar One on One
- Linear targeting (levemente modificado para pequenas voltas para pequenas distâncias)
- Troca de alvo baseado em ser atingido, acerto de projétil, distância e vida
- Troca de potência de projétil baseado em distância e na própria vida
- Anticolisão com a parede (quebrado)


<p align="center">
  <img src="https://user-images.githubusercontent.com/44736064/59646847-11e25400-914f-11e9-82ce-d75b3dc52f4d.gif">
</p>

Demonstração da Reimu trocando de alvo selecionando a menor vida ao acidentalmente "esbarrar" e quando atingida.

## Download .jar
Veja [Releases](https://github.com/g-otn/robocode-reimu/releases).
