import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class DamasV2 {

  //https://www.campusmvp.es/recursos/post/como-cambiar-los-colores-de-la-consola-con-java-y-system-out-println.aspx
  /**
   * Se crea las variables para asignar color al texto y para reiniciar al color predeterminado (REINICIAR_COLOR)
   */
  static String COLOR_ROJO = "\u001B[31m";
  static String COLOR_AZUL = "\u001B[34m";
  static String COLOR_VERDE = "\u001B[32m";
  static String COLOR_AMARILLO = "\u001B[33m";
  static String REINICIAR_COLOR = "\u001B[0m";

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    limpiarPantalla();
    System.out.println("BIENVENID@ AL JUEGO DE DAMAS.");
    System.out.println();
    System.out.println(
      COLOR_ROJO +
      "Desarrollado por: " +
      REINICIAR_COLOR
    );
    System.out.println(COLOR_ROJO + "Karen Sofia Garzonr Rubiano. 6001090" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Mishel Lorena Obando Cardenas. 6001000" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Manuel Felipe Cruz Garcia. 6001061" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Juan Pablo Godoy Gutierrez." + REINICIAR_COLOR);
    System.out.println();
    mostrarReglas();
    System.out.println();
    System.out.println(
      COLOR_AMARILLO + "PARA INICIAR EL JUEGO PRESIONE ENTER." + REINICIAR_COLOR
    );
    scan.nextLine();

    iniciaJuego();
  }

  /**
   * Es la funcion que inicializa el juego
   */
  static void iniciaJuego() {
    int turno = ObtenerTurnoInicial();

    String tablero[][] = CrearTablero();

    do {
      ImprimirTablero(tablero, turno, -1, -1, new ArrayList<String>());

      int totalFichasJugador1 = TotalFichasPorJugador(tablero, 1);
      int totalFichasJugador2 = TotalFichasPorJugador(tablero, 2);

      if (totalFichasJugador1 == 0 || totalFichasJugador2 == 0) {
        System.out.println();
        if (totalFichasJugador1 == 0) {
          System.out.println(
            COLOR_AZUL + "Ha ganado el jugador AZUL" + REINICIAR_COLOR
          );
        } else {
          System.out.println(
            COLOR_ROJO + "Ha ganado el jugador ROJO" + REINICIAR_COLOR
          );
        }
        System.out.println();
        break;
      }

      System.out.println();
      System.out.println(JugadorTurno(turno));
      System.out.println();
      int[] posicion = CapturarFichaMover(tablero, turno);

      int filaFichaSeleccionada = posicion[0];
      int columnaFichaSeleccionada = posicion[1];

      ArrayList<String> casillasALasQueSePuedeMover = PosibleCeldasAMover(
        filaFichaSeleccionada,
        columnaFichaSeleccionada,
        tablero
      );

      ImprimirTablero(
        tablero,
        turno,
        filaFichaSeleccionada,
        columnaFichaSeleccionada,
        casillasALasQueSePuedeMover
      );

      System.out.println();
      int[] casillaAMover = CapturarCeldaDestino(casillasALasQueSePuedeMover);

      int filaDestino = casillaAMover[0];
      int columnaDestino = casillaAMover[1];
      int filaFichaEliminar = casillaAMover[2];
      int columnaFichaEliminar = casillaAMover[3];

      if (
        (turno == 1 && filaDestino == 7) || (turno == 2 && filaDestino == 0)
      ) {
        int[] valoresFicha = ConvertirStringAArray(
          tablero[filaFichaSeleccionada][columnaFichaSeleccionada]
        );
        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];
        tablero[filaDestino][columnaDestino] =
          numeroJugador + "," + numeroFicha + ",1";
      } else {
        tablero[filaDestino][columnaDestino] =
          tablero[filaFichaSeleccionada][columnaFichaSeleccionada];
      }

      tablero[filaFichaSeleccionada][columnaFichaSeleccionada] = "0,0,0";

      if (filaFichaEliminar >= 0) {
        tablero[filaFichaEliminar][columnaFichaEliminar] = "0,0,0";
      }

      if (turno == 2) {
        turno = 1;
      } else {
        turno = 2;
      }
    } while (true);
  }

  static void mostrarReglas() {
    System.out.println(
      "Las damas es un juego para dos personas en un tablero de 64 casillas de 8×8 celdas. El tablero se coloca de manera que cada jugador tenga una casilla blanca en su parte inferior derecha."
    );
    System.out.println();
    System.out.println(
      "Cada jugador dispone de 12 piezas de un mismo color (unas rojas y las otras azules) que al principio de la partida se colocan en las casillas negras de las tres filas más próximas a él."
    );
    System.out.println();
    System.out.println(
      "El objetivo del juego de damas es capturar las fichas del oponente o acorralarlas para que los únicos movimientos que puedan realizar sean los que lleven a su captura."
    );
    System.out.println();
    System.out.println(
      "Se juega por turnos alternos. El turno inicial se obtiene de forma aleatoria. En su turno cada jugador mueve una ficha propia."
    );
    System.out.println();
    System.out.println(
      "Las fichas se mueven (cuando no comen) una posición hacia delante hacia atrás en diagonal a la derecha o a la izquierda, a una posición adyacente vacía."
    );
  }

  static void limpiarPantalla() {
    /**
     * https://es.stackoverflow.com/a/529860
     * Limpiar consola
     */
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  static int TotalFichasPorJugador(
    String[][] tablero,
    int numeroFichaAEvaluar
  ) {
    int contador = 0;

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);

        int numeroJugador = valoresFicha[0];

        if (numeroJugador == numeroFichaAEvaluar) {
          contador = contador + 1;
        }
      }
    }
    return contador;
  }

  /**
   * funcion que dado el num de ficha a mover devuelve los datos que hay en la posicion dada
   * @param numeroFichaAEvaluar
   * @param tablero
   * @param turno
   * @return
   */
  static String ObtenerFichaDadoNumeroDeFicha(
    int numeroFichaAEvaluar,
    String[][] tablero,
    int turno
  ) {
    String valorDevuelto = "";

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);
        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];

        if (numeroFicha == numeroFichaAEvaluar && numeroJugador == turno) {
          valorDevuelto =
            tablero[filas][columnas] + "," + filas + "," + columnas;
          break;
        }
      }

      if (valorDevuelto != "") {
        break;
      }
    }

    return valorDevuelto;
  }

  static int[] CapturarFichaMover(String[][] tablero, int turno) {
    Scanner scan = new Scanner(System.in);

    int posiciones[] = new int[2];

    do {
      System.out.print("Digite la ficha que desea mover: ");
      int numeroFichaIngresada = scan.nextInt();

      if (numeroFichaIngresada >= 1 && numeroFichaIngresada <= 12) {
        String valorEnLaFichaSeleccionada = ObtenerFichaDadoNumeroDeFicha(
          numeroFichaIngresada,
          tablero,
          turno
        );

        /*
         * [1,10,0,2,3]
         * [numero jugador, numero ficha, es dama, fila, columna]
         */
        int[] valorArrayFichaSeleccionada = ConvertirStringAArray(
          valorEnLaFichaSeleccionada
        );

        int filaFichaSeleccionada = valorArrayFichaSeleccionada[3];
        int columnaFichaSeleccionada = valorArrayFichaSeleccionada[4];

        /**
         * guarda el total de posiciones donde se puede mover la ficha seleccionada
         */
        int totalDePosicionesAMover = PosibleCeldasAMover(
          filaFichaSeleccionada,
          columnaFichaSeleccionada,
          tablero
        )
          .size();

        /**
         * Si el total de posiciones a mover == 0 no se puede mover ya que esta bloqueada
         */
        if (totalDePosicionesAMover == 0) {
          System.out.println("La ficha seleccionada no se puede mover");
        } else {
          /**
           * se devuelven las coordenadas de la ficha que se ha seleccionado
           */
          posiciones[0] = filaFichaSeleccionada;
          posiciones[1] = columnaFichaSeleccionada;
          break;
        }
      } else {
        System.out.println("El numero de ficha ingresado no es valido");
      }
    } while (true);

    return posiciones;
  }

  /**
   * Funcion que muestra que jugador tiene el turno y se le asigna su respectivo color
   * @param turno
   * @return
   */
  static String JugadorTurno(int turno) {
    String textoTurno = "Tiene el turno el jugador: ";
    if (turno == 1) {
      textoTurno = textoTurno + COLOR_ROJO + " Rojo " + REINICIAR_COLOR;
    } else {
      textoTurno = textoTurno + COLOR_AZUL + " Azul " + REINICIAR_COLOR;
    }

    return textoTurno;
  }

  static int[] CapturarCeldaDestino(
    ArrayList<String> casillasALasQueSePuedeMover
  ) {
    Scanner scan = new Scanner(System.in);

    int totalCaillasAMover = casillasALasQueSePuedeMover.size();
    int[] coordenadaAMoverse;

    do {
      System.out.print(
        "Digite la celda a la que quiere mover la ficha seleccionada: "
      );

      int posicionesAMover = scan.nextInt();

      if (posicionesAMover >= 1 && posicionesAMover <= totalCaillasAMover) {
        coordenadaAMoverse =
          ConvertirStringAArray(
            casillasALasQueSePuedeMover.get(posicionesAMover - 1)
          );

        break;
      } else {
        System.out.println(
          "El numero ingresado es invalido, el valor maximo es: " +
          totalCaillasAMover
        );
      }
    } while (true);
    return coordenadaAMoverse;
  }

  static ArrayList<String> PosibleCeldaAMoverPorUsuario(
    String[][] tablero,
    int numeroJugador,
    int fila,
    int columna,
    int incrementoFila
  ) {
    ArrayList<String> espaciosDisponibles = new ArrayList<String>();

    for (int contador = 1; contador <= 2; contador++) {
      int nuevaColumna = 0;

      int nuevaFila = fila + incrementoFila;

      boolean nuevaFilaEnRango = PosicionRango(nuevaFila + 1);

      if (nuevaFilaEnRango == true) {
        if (contador == 1) {
          nuevaColumna = columna - 1;
        } else {
          nuevaColumna = columna + 1;
        }

        boolean nuevaColumnaEnRango = PosicionRango(nuevaColumna + 1);

        if (nuevaColumnaEnRango == true) {
          int[] valorEnLaCeldaDestino = ConvertirStringAArray(
            tablero[nuevaFila][nuevaColumna]
          );

          /*variable que guarda valor que hay en la celda de destino */
          int valorEnLaCelda = valorEnLaCeldaDestino[0];

          if (valorEnLaCelda == 0) {
            espaciosDisponibles.add(nuevaFila + "," + nuevaColumna + ",-1,-1");
          } else {
            if (valorEnLaCelda != numeroJugador) {
              int nuevaFilaVacia = nuevaFila + incrementoFila;

              if (PosicionRango(nuevaFilaVacia + 1)) {
                int nuevaColumnaVacia = 0;

                if (contador == 1) {
                  nuevaColumnaVacia = nuevaColumna - 1;
                } else {
                  nuevaColumnaVacia = nuevaColumna + 1;
                }

                if (PosicionRango(nuevaColumnaVacia + 1)) {
                  int[] valorCasillaVacia = ConvertirStringAArray(
                    tablero[nuevaFilaVacia][nuevaColumnaVacia]
                  );

                  /*variable que guarda valor que hay en la celda de destino */
                  int valorEnColumnaVacia = valorCasillaVacia[0];

                  if (valorEnColumnaVacia == 0) {
                    espaciosDisponibles.add(
                      nuevaFilaVacia +
                      "," +
                      nuevaColumnaVacia +
                      "," +
                      nuevaFila +
                      "," +
                      nuevaColumna
                    );
                  }
                }
              }
            }
          }
        }
      }
    }
    return espaciosDisponibles;
  }

  /**
   * funcion que valida a las celdas que se podria mover la ficha
   * @param fila
   * @param columna
   * @param tablero
   * @return
   */
  static ArrayList<String> PosibleCeldasAMover(
    int fila,
    int columna,
    String[][] tablero
  ) {
    /*
     * variable que va a guardar cuales son los espacios a los que se puede mover la fi
     */
    ArrayList<String> espaciosDisponibles = new ArrayList<String>();

    /* valoresFichas= contiene la info del tablero dadas las coordenadas */
    int[] valoresFicha = ConvertirStringAArray(tablero[fila][columna]);
    /*
     * [1, 1, 0] -> [numeroJugador, numeroFicha, esdama]
     */
    int numeroJugador = valoresFicha[0];
    int esDama = valoresFicha[2];

    if (esDama == 0) {
      int incrementoFila = 0;
      if (numeroJugador == 1) {
        incrementoFila = 1;
      } else {
        incrementoFila = -1;
      }
      espaciosDisponibles =
        PosibleCeldaAMoverPorUsuario(
          tablero,
          numeroJugador,
          fila,
          columna,
          incrementoFila
        );
    } else {
      ArrayList<String> espaciosAbajo = PosibleCeldaAMoverPorUsuario(
        tablero,
        numeroJugador,
        fila,
        columna,
        1
      );
      ArrayList<String> espaciosArriba = PosibleCeldaAMoverPorUsuario(
        tablero,
        numeroJugador,
        fila,
        columna,
        -1
      );

      for (int i = 0; i < espaciosAbajo.size(); i++) {
        espaciosDisponibles.add(espaciosAbajo.get(i));
      }

      for (int i = 0; i < espaciosArriba.size(); i++) {
        espaciosDisponibles.add(espaciosArriba.get(i));
      }
    }

    return espaciosDisponibles;
  }

  /**
   * Valida que la coordenada dad este dentro del rango del tablero (1,8)
   * @param posicion
   * @return
   */
  static boolean PosicionRango(int posicion) {
    return posicion >= 1 && posicion <= 8;
  }

  /**
   * obtener de forma aleatoria que jugador va a empezar
   * @return
   */
  static int ObtenerTurnoInicial() {
    Random aleatorio = new Random();
    int eleccionTurno = aleatorio.nextInt(2 - 1 + 1) + 1;
    return eleccionTurno;
  }

  /**
   * Funcion que retorna el estado inicial del tablero
   * @return
   */
  static String[][] CrearTablero() {
    String tablero[][] = new String[8][8];
    int contadorJugador1 = 0;
    int contadorJugador2 = 0;

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        // valida si la casilla esta vacia, si lo esta su valor = 0
        boolean esCasillaVacia = (filas + columnas) % 2 == 0;

        if (esCasillaVacia == true) {
          tablero[filas][columnas] = "0,0,0";
        } else {
          if (filas <= 2 || filas >= 5) {
            String informacionFicha = "";
            /**
             * va a parecer 0 si no cumple que sea <= 2
             * y decir que si cumple seria el jugador 1 o el jugador 2
             */
            if (filas <= 2) {
              contadorJugador1 = contadorJugador1 + 1;
              informacionFicha = "1," + contadorJugador1 + ",0";
            } else {
              contadorJugador2 = contadorJugador2 + 1;
              informacionFicha = "2," + contadorJugador2 + ",0";
            }
            /**
             * se guarda valor en el Array/Matriz
             * informacionFicha guarda la info de la siguiente manera:
             * 1,1,0 donde el primer 1 es el valor del jugador (1 o 2)
             * el siguiente 1 corresponde al numero de la ficha min 1 max 12,
             * y el 0 corresponde a si es una dama
             */
            tablero[filas][columnas] = informacionFicha;
          } else {
            tablero[filas][columnas] = "0,0,0";
          }
        }
      }
    }

    return tablero;
  }

  /** funcion que dado un string separado por , se convierte a un array numerico */
  static int[] ConvertirStringAArray(String valorString) {
    // split: parte un string dado un caracter de separacion (,)
    String[] valores = valorString.split(",");
    int[] resultado = new int[valores.length];

    for (int i = 0; i < valores.length; i++) {
      resultado[i] = Integer.parseInt(valores[i]);
    }

    return resultado;
  }

  /**
   * Funcion que imprime el tablero en la consola
   * @param tablero
   * @param turno
   */
  static void ImprimirTablero(
    String[][] tablero,
    int turno,
    int filaFichaSeleccionada,
    int columnaFichaSeleccionada,
    ArrayList<String> casillasALasQueSePuedeMover
  ) {
    char muestraDama = '\u2654'; // Representa el símbolo del rey
    char muestraToken = '\u2686';

    limpiarPantalla();

    System.out.println();
    int totalFichasJugador1 = TotalFichasPorJugador(tablero, 1);
    int totalFichasJugador2 = TotalFichasPorJugador(tablero, 2);

    System.out.println(
      COLOR_ROJO + " ROJO: " + totalFichasJugador1 + " " + REINICIAR_COLOR
    );
    System.out.println(
      COLOR_AZUL + " AZUL: " + totalFichasJugador2 + " " + REINICIAR_COLOR
    );

    System.out.println();

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);
        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];
        int esDama = valoresFicha[2];

        String muestraFicha = "";

        if (esDama == 1) {
          muestraFicha = " " + muestraDama + " ";
        } else {
          muestraFicha = " ◉ ";
        }
        /**
         * Valida si se muestra una casilla negra, si la suma de fila y columna es par
         */
        boolean esCasillaNegra = (filas + columnas) % 2 == 0;
        if (esCasillaNegra == true) {
          System.out.print("█████");
        } else {
          if (numeroJugador == 1 || numeroJugador == 2) {
            String colorFicha = "";

            if (numeroJugador == 1) {
              colorFicha = COLOR_ROJO;
            } else {
              colorFicha = COLOR_AZUL;
            }

            String valorCelda = "";

            if (numeroJugador == turno) {
              if (filaFichaSeleccionada == -1) {
                /*
                evalua la cnatidad de casillas que puede mover la ficha
                */
                int totalDePosicionesAMover = PosibleCeldasAMover(
                  filas,
                  columnas,
                  tablero
                )
                  .size();
                /**
                 * si el total de posiciones a mover es == 0 muestra el tiken ya que no se puede mover
                 */
                if (totalDePosicionesAMover == 0) {
                  valorCelda = muestraFicha;
                } else {
                  // sino muestra el numero en el tablero para que el usuario sepa que puede seleccionarlo
                  valorCelda = numeroFicha + " ";
                  /*
                   * lengh para saber el tamño del string, si es 2
                   * le agrgamos espacios al inicio y al fnal
                   */
                  if (valorCelda.length() == 2) {
                    valorCelda = " " + numeroFicha + " ";
                  }
                }
              } else {
                if (
                  filas == filaFichaSeleccionada &&
                  columnas == columnaFichaSeleccionada
                ) {
                  valorCelda = muestraFicha;
                  colorFicha = COLOR_AMARILLO;
                } else {
                  valorCelda = muestraFicha;
                }
              }
            } else {
              valorCelda = muestraFicha;
            }

            System.out.print(
              colorFicha + " " + valorCelda + " " + REINICIAR_COLOR
            );
          } else {
            String celdaStringSeleccionada = "     ";
            /**
             * si filaseleccionada es diferente a -1 quiere decir que ya hay una ficha seleccionada
             */
            if (filaFichaSeleccionada != -1) {
              for (int i = 0; i < casillasALasQueSePuedeMover.size(); i++) {
                /** se extrae las filas y las columnas de las posibles posiciones a las cuales se puede mover la ficha */
                int[] posicionesEleccion = ConvertirStringAArray(
                  casillasALasQueSePuedeMover.get(i)
                );
                int filaPosibleSeleccion = posicionesEleccion[0];
                int columnaPosibleSeleccion = posicionesEleccion[1];

                /**
                 * se valida si la fila y columna es igual a las posibles posiciones
                 */
                if (
                  filas == filaPosibleSeleccion &&
                  columnas == columnaPosibleSeleccion
                ) {
                  celdaStringSeleccionada =
                    COLOR_AMARILLO + "  " + (i + 1) + "  " + REINICIAR_COLOR;
                  break;
                }
              }
            }
            System.out.print(celdaStringSeleccionada);
          }
        }
      }
      System.out.println();
    }
  }
}
