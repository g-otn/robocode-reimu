/**
 * 6º Campeonato Robocode CPS
 * @author Gabriel Otani Pereira, Thiago Bezerra, Wagner Santos
 * @since Agosto 2018
 */
package Alpha;

import robocode.*;
import static robocode.util.Utils.*;
import java.awt.Color;

/**
 * Reimu
 */
public class Reimu extends AdvancedRobot {
  
  /*
    Variáveis de comportamento
  */
  // Movimento
  double d = 150.0;
  double dir = 1;
  // Condições de execução de movimento
  boolean desviando = false;
  boolean saindoDaParede = false;
  
  
  /*
    Variáveis para armazenamento de informações sobre o alvo
  */
  String alvo = "nenhum"; // Guarda o nome do alvo
  double vidaAlvo; // Vida do alvo
  double distAlvo; // Distância do alvo
  
  /*
    Variáveis para troca de alvo
  */
  // Variáveis de controle de escolha de alvo
  boolean escolhendoAlvo = false;
  // Variáveis de armazenamento de informações sobre outros robôs
  String outros[];
  double disOutros[];
  
  
  /*
    Inicia o robô, colocando configurações iniciais e escolhendo o primeiro alvo
  */
  public void run() {
    setColors(Color.RED, Color.BLACK, Color.RED, new Color(200,25,0), new Color(150,150,200));
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    setTurnRadarRight(Double.POSITIVE_INFINITY); // Procura por alvos
    do {
      // Nunca para de andar
      setAhead((distAlvo / 4 + 25) * dir);
      

      /*
        Atualização de status
      */
      // Marca como não desviando se já acabou o movimento de desvio
      if (desviando && getTurnRemaining() == 0) {
        desviando = false;
      }

      /* 
        Distanciamento da parede
      */
      // Impede de bater na parede
      if (!saindoDaParede && vaiBater(3)) { // Não tenta sair da parede se já estiver saindo dela
        dir *= -1;
        saindoDaParede = true; // Marca como estando perto da parede
      } else if (!vaiBater(3.2)) {
        saindoDaParede = false; // Marca como não estando perto da parede
      }
      
      execute();
    } while (true);
  }
  
  
  /*
    É chamada quando algum robô entra no arco do radar
  */
  public void onScannedRobot(ScannedRobotEvent e) {
    
    /*
      Ajuste de inclinação
    */
    // Arruma o ângulo do corpo do robô se não estiver ou batendo na parede
    if (!vaiBater(0.6)) {
      setTurnRight(e.getBearing() + 90 - 30*dir);
    }
    
    
    /*
      Realizador de manobras de desvio
    */
    // Só desvia se o alvo atirou: Se perdeu energia que é perdida por tiro (de 0.1 até 3, sem ser o dano de colisão (0.6));
    // Ou se já não está no começo de um desvio (para não ficar mudando de direção muito rápido, o que faz ficar parado)
    if (!saindoDaParede &&
        vidaAlvo - e.getEnergy() >= Rules.MIN_BULLET_POWER && // Entre 0.1
        vidaAlvo - e.getEnergy() <= Rules.MAX_BULLET_POWER && // e 3
        !(vidaAlvo - e.getEnergy() > 0.57 && vidaAlvo - e.getEnergy() < 0.63) // dano causado por Ram
        ) {
      out.println("Alvo perdeu energia " + Math.round(vidaAlvo * 10) / 10.0 + "->" + Math.round(e.getEnergy() * 10) / 10.0 + " (" + (vidaAlvo - e.getEnergy()) + "), pode ter atirado! Trocando de direção");
      desviando = true;
      dir *= -1;
    }
    
    
    // Pega o ângulo necessário para alinhar o corpo, arma ou radar no inimigo
    // Usando o getHeading(), já que o e.getBearing() é relativo a ele,
    // a partir do absBearing, você subtrai com o que será alinhado
    double absBearing = e.getBearing() + getHeading();

    
    /*
      Ajustar radar
    */
    setTurnRadarRight(normalRelativeAngleDegrees((absBearing - getRadarHeading())*2));

    
    /*
      Ajustar arma
    */
    setTurnGunRightRadians(ajustarArma(e)*0.85);
    
    
    /*
      Atirar
    */
    setFire(calcularPoderTiro(e.getDistance()));
    
    
    
    /*
      Guarda informação sobre o alvo (para outros eventos)
    */
    vidaAlvo = e.getEnergy();
    distAlvo = e.getDistance();
  }
  
  
  /*
    É chamada quando for atingido por uma bala
  */
  public void onHitByBullet(HitByBulletEvent e) {
    dir *= -1;
    setTurnRight(e.getBearing() + 90 - 30*dir);
    
    if (!e.getName().equals(alvo)) {
      out.println("Novo alvo por ser atingindo! " + e.getName());
      alvo = e.getName();
      escolhendoAlvo = false;
      // Procura o novo alvo
      double radarBearing = normalRelativeAngleDegrees((e.getBearing() + getHeading()) - getRadarHeading());
      // Gira o radar na direção que o encontrará mais rápido
      if (radarBearing > 0) {
        setTurnRadarRight(Double.POSITIVE_INFINITY);
      } else {
        setTurnRadarRight(Double.NEGATIVE_INFINITY);
      }
    }
  }
  
  
  /*
    É chamada quando bater em um robô
  */
  public void onHitRobot(HitRobotEvent e) {
    dir *= -1;
    setTurnRight(e.getBearing() + 90 - 30*dir);
    if (!e.getName().equals(alvo)) {
      out.println("Novo alvo por colisão! " + e.getName());
      escolhendoAlvo = false;
      // Procura o novo alvo
      double radarBearing = normalRelativeAngleDegrees((e.getBearing() + getHeading()) - getRadarHeading());
      // Gira o radar na direção que o encontrará mais rápido
      if (radarBearing > 0) {
        setTurnRadarRight(Double.POSITIVE_INFINITY);
      } else {
        setTurnRadarRight(Double.NEGATIVE_INFINITY);
      }
    }
  }
  
  
  /*
    É chamada quando bater na parede
  */
  public void onHitWall(HitWallEvent e) {
    saindoDaParede = true; // Impede de desviar perto da parede (evita movimentos errados e ficar preso)
    
    /*
      Ajuste de ângulo
    */
    if (dir == -1 && Math.abs(e.getBearing()) >= 160.0) {
      // Se estiver de costas para a parede
      dir = 1; // Sair de perto da parede andando de frente
    } else if (dir == 1 && Math.abs(e.getBearing()) <= 20.0) {
      // Se estiver de frente para a parede
      dir = -1; // Sair de perto da parede andando de costas
    } else {
      // Se estiver de lado para a parede
      if (dir == 1) {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()+10));
        dir = -1;
      } else {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()+170));
        dir = 1;
      }
    }
  }
  
  
  /*
    É chamada quando algum robô morrer
  */
  public void onRobotDeath(RobotDeathEvent e) {
    // Busca por outro alvo caso o atual morra
    setTurnRadarRight(Double.POSITIVE_INFINITY);
  }
  
  
  /*
    Ajusta a arma no inimigo compensando sua velocidade
  */
  double ajustarArma(ScannedRobotEvent e) {
    if (e.getEnergy() != 0.0) {
      double absBearingRad = getHeadingRadians() + e.getBearingRadians();
      // Calculo de onde o inimigo
      double compensacaoLinear = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearingRad) // Quanto o inmigo vai andar
              / Rules.getBulletSpeed(calcularPoderTiro(e.getDistance())); // Quanto tempo a bala demora para alcançar
      
      // Modificação de compensação linear (para não perder muito tempo virando a arma se estiver perto)
      if (e.getDistance() <= 12*10) {
        // Se estiver perto
        compensacaoLinear *= 0.6;
      } else if (e.getDistance() <= 12*5) {
        // Se estiver muito perto
        compensacaoLinear *= 0.4;
      }
      
      return normalRelativeAngle(absBearingRad - getGunHeadingRadians() + compensacaoLinear);
    } else {
      // Se o inimigo estiver com 0 de energia (Disabled), ele fica parado mas a velocidade pode estar maior que 0
      // então é preciso desligar a mira lateral, que usa a velocidade para determinar o ângulo
      return normalRelativeAngle(getHeadingRadians()+ e.getBearingRadians()- getGunHeadingRadians());
    }
  }
  
  
  /*
    Calcula a potência do tiro baseado na distância do alvo e na própria vida
  */
  double calcularPoderTiro(double dAlvo) {
    if (vidaAlvo != 0.0) {
      return Math.max(getBattleFieldHeight(), getBattleFieldWidth()) / dAlvo;
    } else {
      return 0.1;
    }
  }
  
  
  /*
    Checa se o robô vai bater na parede
  */
  boolean vaiBater(double margem) {
    // Retorna verdadeiro se a posição estiver chegando perto das paredes, caso contrário retorna falso
    return getX() + getHeight()*margem >= getBattleFieldWidth()
            || getX() - getHeight()*margem <= 0.0
            || getY() + getHeight()*margem >= getBattleFieldHeight()
            || getY() - getHeight()*margem <= 0.0;
  }
}
