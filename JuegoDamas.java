import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JuegoDamas {

  // https://www.campusmvp.es/recursos/post/como-cambiar-los-colores-de-la-consola-con-java-y-system-out-println.aspx
  /**
   * Se crea las variables para asignar color al texto y para reiniciar al color
   * predeterminado (REINICIAR_COLOR)
   */
  static String COLOR_ROJO = "\u001B[31m";
  static String COLOR_AZUL = "\u001B[34m";
  static String COLOR_AMARILLO = "\u001B[33m";
  static String REINICIAR_COLOR = "\u001B[0m";

  public static void main(String[] args) {
    /**
     * Se invoca una funcion para limpiar la pantalla o consola
     */
    limpiarPantalla();

    /**
     * Funcion que va a mostrar las reglas del juego
     */
    mostrarReglas();

    /**
     * Funcion que inica el juego
     */
    iniciaJuego();
  }

  /**
   * Es la funcion que inicializa el juego
   */
  static void iniciaJuego() {
    /**
     * Se obtiene de forma aleatoria el turno inicial (1 o 2)
     */
    int turno = ObtenerTurnoInicial();

    /**
     * Crear un array que contiene la info inicial del tablero (8x8)
     */
    String tablero[][] = CrearTablero();

    /**
     * Se crea un ciclo infinito con el do while
     */
    do {
      ImprimirTablero(tablero, turno, -1, -1, new ArrayList<String>());

      /**
       * se obtiene la totalidad de fichas por jugador
       */
      int totalFichasJugador1 = TotalFichasPorJugador(tablero, 1);
      int totalFichasJugador2 = TotalFichasPorJugador(tablero, 2);

      /**
       * si la totalidad de fichas de uno de los jugadores es 0, se termina el juego
       */
      if (totalFichasJugador1 == 0 || totalFichasJugador2 == 0) {
        System.out.println();
        /**
         * si el total de fichas del jugador 1 = 0, gana el jugador 2
         */
        if (totalFichasJugador1 == 0) {
          System.out.println(
              COLOR_AZUL + "Ha ganado el jugador AZUL" + REINICIAR_COLOR);
        } else {
          System.out.println(
              COLOR_ROJO + "Ha ganado el jugador ROJO" + REINICIAR_COLOR);
        }
        System.out.println();
        // rompe el ciclo do while
        break;
      }

      System.out.println();
      // se obtiene un string que improme que jugador tiene el turno actual
      String turnoDelJugadorActual = JugadorTurno(turno);
      System.out.println(turnoDelJugadorActual);
      System.out.println();

      /**
       * se obtiene la posicion de fila y columna de la ficha seleccionada
       */
      int[] posicion = CapturarFichaMover(tablero, turno);

      int filaFichaSeleccionada = posicion[0];
      int columnaFichaSeleccionada = posicion[1];

      /**
       * obtiene la posibles posiciones a las que se puede mover la ficha seleccionada
       */
      ArrayList<String> casillasALasQueSePuedeMover = PosibleCeldasAMover(
          filaFichaSeleccionada,
          columnaFichaSeleccionada,
          tablero);

      /**
       * se obtiene el total de casillas a las cuales se podria mover la ficha
       * seleccionada
       */
      int tamanoCasillasAMover = casillasALasQueSePuedeMover.size();

      /**
       * array donde se guardara la fila y la columna a donde se va a mover la ficha
       * seleccionada y la la fila y columna de la ficha que se va a eliminar (si
       * existe)
       */
      int[] casillaAMover;

      /**
       * si solo existe una casilla a mover se obvtiene la posicion que hay en la
       * posicion 0
       */
      if (tamanoCasillasAMover == 1) {
        casillaAMover = ConvertirStringAArray(casillasALasQueSePuedeMover.get(0));
      } else {
        /**
         * se vuelve a imprimir el tablero enviando la posicion fila y columna de la
         * ficha seleccionada y las posibles posiciones a las que se puede mover la
         * ficha
         */
        ImprimirTablero(
            tablero,
            turno,
            filaFichaSeleccionada,
            columnaFichaSeleccionada,
            casillasALasQueSePuedeMover);
        System.out.println();

        /**
         * [4,3,-1,-1]
         * se obtiene la fila y columna a la que se puede mover la ficha y fila y
         * columna de la ficha que se puede eliminar
         */
        casillaAMover = CapturarCeldaDestino(casillasALasQueSePuedeMover);
      }

      /**
       * guarda la fila y la columna a la cual se va a mover la ficha
       * filaDestino
       */
      int filaDestino = casillaAMover[0];
      int columnaDestino = casillaAMover[1];

      /**
       * guarda la fila y columna de la ficha que se va a eliminar
       */
      int filaFichaEliminar = casillaAMover[2];
      int columnaFichaEliminar = casillaAMover[3];

      /**
       * variables que guardan si la ficha que se ha movido se puede convertir en dama
       * si es el jugador 1 es porque ha llegado a la posicion 7
       * si es el jugador 2 es porque ha llegado a la posicion 0
       */
      boolean jugadorUnoEsDama = turno == 1 && filaDestino == 7;
      boolean jugadorDosEsDama = turno == 2 && filaDestino == 0;

      /**
       * se valida si la ficha puede ser dama
       */
      if (jugadorUnoEsDama || jugadorDosEsDama) {

        /*
         * "1,3,0" -> [1,3,0]
         */
        int[] valoresFicha = ConvertirStringAArray(
            tablero[filaFichaSeleccionada][columnaFichaSeleccionada]);

        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];

        /**
         * "1,3,1"
         * se concatena los valores del numero de jugador, el num de ficha y se adiciona
         * 1 para indicar que ya es dama
         */
        tablero[filaDestino][columnaDestino] = numeroJugador + "," + numeroFicha + ",1";
      } else {
        tablero[filaDestino][columnaDestino] = tablero[filaFichaSeleccionada][columnaFichaSeleccionada];
      }

      /**
       * en la celda de la ficha seleccionada se establece el valor de 0,0,0 porque ya
       * es vacio
       */
      tablero[filaFichaSeleccionada][columnaFichaSeleccionada] = "0,0,0";

      /**
       * si la fila a eliminar es >= 0 quiere decir que se puede eliminar
       * si el valor es -1 significa que no se va a eliminar
       */
      if (filaFichaEliminar >= 0) {
        tablero[filaFichaEliminar][columnaFichaEliminar] = "0,0,0";
      }

      /**
       * se determina el siguiente turno
       */
      if (turno == 2) {
        turno = 1;
      } else {
        turno = 2;
      }
    } while (true);

  }

  /**
   * Función que muestra las reglas del juego y pide una accion para iniciar el
   * juego (enter)
   */
  static void mostrarReglas() {
    Scanner scan = new Scanner(System.in);
    System.out.println("BIENVENID@ AL JUEGO DE DAMAS.");
    System.out.println();
    System.out.println(
        COLOR_ROJO +
            "Desarrollado por: " +
            REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Karen Sofia Garzon Rubiano. 6001090" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Mishel Lorena Obando Cardenas. 6001000" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Manuel Felipe Cruz Garcia. 6001061" + REINICIAR_COLOR);
    System.out.println(COLOR_ROJO + "Juan Pablo Godoy Gutierrez." + REINICIAR_COLOR);
    System.out.println();

    System.out.println(
        "Las damas es un juego para dos personas en un tablero de 64 casillas de 8×8 celdas. El tablero se coloca de manera que cada jugador tenga una casilla blanca en su parte inferior derecha.");
    System.out.println();
    System.out.println(
        "Cada jugador dispone de 12 piezas de un mismo color (unas rojas y las otras azules) que al principio de la partida se colocan en las casillas negras de las tres filas más próximas a él.");
    System.out.println();
    System.out.println(
        "El objetivo del juego de damas es capturar las fichas del oponente o acorralarlas para que los únicos movimientos que puedan realizar sean los que lleven a su captura.");
    System.out.println();
    System.out.println(
        "Se juega por turnos alternos. El turno inicial se obtiene de forma aleatoria. En su turno cada jugador mueve una ficha propia.");
    System.out.println();
    System.out.println(
        "Las fichas se mueven (cuando no comen) una posición hacia delante hacia atrás en diagonal a la derecha o a la izquierda, a una posición adyacente vacía.");

    System.out.println();
    System.out.println(
        COLOR_AMARILLO + "PARA INICIAR EL JUEGO PRESIONE ENTER." + REINICIAR_COLOR);
    /**
     * Captura un valor por teclado del usuario para iniciar el juego, hace una
     * interrupción
     */
    scan.nextLine();
  }

  /**
   * Funcion que limpia la consola
   */
  static void limpiarPantalla() {
    /**
     * https://es.stackoverflow.com/a/529860
     * Limpiar consola
     */
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  /**
   * Funcion que calcula el numero de fichas disponibles que tiene cada jugador
   * numeroJugadorAEvaluar = 1 o 2
   */
  static int TotalFichasPorJugador(String[][] tablero, int numeroJugadorAEvaluar) {
    /**
     * Guarda el total de fichas por jugador
     */
    int contador = 0;

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        // "1,1,0" -> [1,1,0]
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);

        int numeroJugador = valoresFicha[0];

        if (numeroJugador == numeroJugadorAEvaluar) {
          contador = contador + 1;
        }
      }
    }
    return contador;
  }

  /**
   * funcion que dado el num de ficha a mover devuelve los datos que hay en la
   * posicion dada
   * 
   * @param numeroFichaAEvaluar
   * @param tablero
   * @param turno
   * @return
   */
  static String ObtenerFichaDadoNumeroDeFicha(
      int numeroFichaAEvaluar,
      String[][] tablero,
      int turno) {
    String valorDevuelto = "";

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        /**
         * se obtiene el valor que esta en la posicion dada
         * "2,2,0" -> [2,2,0]
         */
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);
        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];

        if (numeroFicha == numeroFichaAEvaluar && numeroJugador == turno) {
          /**
           * se devulve todo lo que existe en la posicion dada, ademas de la fila y su
           * columna
           * "2,2,0,5,2"
           */
          valorDevuelto = tablero[filas][columnas] + "," + filas + "," + columnas;
          break;
        }
      }

      if (valorDevuelto != "") {
        break;
      }
    }

    return valorDevuelto;
  }

  /**
   * Funcion que captura la seleccion de la ficha que el usuario va a mover
   * 
   * @param tablero
   * @param turno
   * @return
   */
  static int[] CapturarFichaMover(String[][] tablero, int turno) {
    Scanner scan = new Scanner(System.in);

    /**
     * array que va a guardar la fila y la columna de la ficha seleccionada
     */
    int posiciones[] = new int[2];

    do {
      System.out.print("Digite la ficha que desea mover: ");
      int numeroFichaIngresada = scan.nextInt();

      // se valida que el valor ingresado por el usuario este en el rango de 1 a 12
      if (numeroFichaIngresada >= 1 && numeroFichaIngresada <= 12) {
        /**
         * funcion que dado el num de la ficha devulve la info de lo que hay en esa
         * posicion y fila y columna
         */
        String valorEnLaFichaSeleccionada = ObtenerFichaDadoNumeroDeFicha(
            numeroFichaIngresada,
            tablero,
            turno);

        if (valorEnLaFichaSeleccionada == "") {
          System.out.println("El numero de ficha seleccionado ya no existe");
        } else {
          /*
           * [1,10,0,2,3]
           * [numero jugador, numero ficha, es dama, fila, columna]
           */
          int[] valorArrayFichaSeleccionada = ConvertirStringAArray(
              valorEnLaFichaSeleccionada);

          int filaFichaSeleccionada = valorArrayFichaSeleccionada[3];
          int columnaFichaSeleccionada = valorArrayFichaSeleccionada[4];

          /**
           * guarda el total de posiciones donde se puede mover la ficha seleccionada
           */
          int totalDePosicionesAMover = PosibleCeldasAMover(
              filaFichaSeleccionada,
              columnaFichaSeleccionada,
              tablero)
              .size();

          /**
           * Si el total de posiciones a mover == 0 no se puede mover ya que esta
           * bloqueada
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
        }
      } else {
        System.out.println("El numero de ficha ingresado no es valido");
      }
    } while (true);

    return posiciones;
  }

  /**
   * Funcion que muestra que jugador tiene el turno y se le asigna su respectivo
   * color
   * 
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

  /**
   * funcion que captura la seleccion de la celda a la cual se va a mover l ficha
   * 
   * @param casillasALasQueSePuedeMover
   * @return
   */
  static int[] CapturarCeldaDestino(
      ArrayList<String> casillasALasQueSePuedeMover) {
    Scanner scan = new Scanner(System.in);

    /**
     * guarda el total de casillas a las que se puede mover la ficha
     */
    int totalCaillasAMover = casillasALasQueSePuedeMover.size();

    int[] coordenadaAMoverse;

    do {
      System.out.print(
          "Digite la celda a la que quiere mover la ficha seleccionada: ");

      int posicionesAMover = scan.nextInt();

      if (posicionesAMover >= 1 && posicionesAMover <= totalCaillasAMover) {
        /**
         * "4,3,-1,-1" -> [4,3,-1,-1]
         */
        coordenadaAMoverse = ConvertirStringAArray(
            casillasALasQueSePuedeMover.get(posicionesAMover - 1));

        break;
      } else {
        System.out.println(
            "El numero ingresado es invalido, el valor maximo es: " +
                totalCaillasAMover);
      }
    } while (true);
    return coordenadaAMoverse;
  }

  /**
   * funcion que devuelve las celdas a las que se podria mover una ficha
   */
  static ArrayList<String> PosibleCeldaAMoverPorUsuario(
      String[][] tablero,
      int numeroJugador,
      int fila,
      int columna,
      int incrementoFila) {

    ArrayList<String> espaciosDisponibles = new ArrayList<String>();

    /**
     * se itera dos veces ya que los posibles movimientos de la fila son dos (izq o
     * der)
     */
    for (int contador = 1; contador <= 2; contador++) {
      int nuevaColumna = 0;

      int nuevaFila = fila + incrementoFila;

      boolean nuevaFilaEnRango = PosicionRango(nuevaFila + 1);

      if (nuevaFilaEnRango == true) {
        /**
         * se obtiene la columna a partir del contador, si contador es 1 se evalua la
         * columna de la izq si es 2 se evalua a la der
         */
        if (contador == 1) {
          nuevaColumna = columna - 1;
        } else {
          nuevaColumna = columna + 1;
        }

        /**
         * se evalua que la nueva columna este dentro del rango del tablero
         */
        boolean nuevaColumnaEnRango = PosicionRango(nuevaColumna + 1);

        if (nuevaColumnaEnRango == true) {
          // "1,5,0" -> [1,5,0]
          int[] valorEnLaCeldaDestino = ConvertirStringAArray(
              tablero[nuevaFila][nuevaColumna]);

          /* variable que guarda valor que hay en la celda de destino */
          int valorEnLaCelda = valorEnLaCeldaDestino[0];

          // se valida si la celda esta vacia
          if (valorEnLaCelda == 0) {
            /**
             * se guarda la fila y la columna a la cual se podria mover la ficha
             * los valores -1,-1 determinan la fila y la columna donde estaria la ficha de
             * un contrincante que se eliminaria, en este caso es -1,-1 por que no se
             * elimina ninguna
             * add = agrega un elemento en el arrayList
             * get = obtiene un dato de un arrayList
             */
            espaciosDisponibles.add(nuevaFila + "," + nuevaColumna + ",-1,-1");
          } else {
            if (valorEnLaCelda != numeroJugador) {
              /**
               * se establece la fila final en la que quedaria la ficha cuando se elimina la
               * ficha de un contrincante
               */
              int nuevaFilaVacia = nuevaFila + incrementoFila;

              /**
               * valida que esa nueva fila este en el rango del tablero
               */
              if (PosicionRango(nuevaFilaVacia + 1)) {
                /**
                 * se valida la columna de destino cuando se elimina un token de un contrincante
                 */
                int nuevaColumnaVacia = 0;

                if (contador == 1) {
                  nuevaColumnaVacia = nuevaColumna - 1;
                } else {
                  nuevaColumnaVacia = nuevaColumna + 1;
                }

                if (PosicionRango(nuevaColumnaVacia + 1)) {
                  int[] valorCasillaVacia = ConvertirStringAArray(
                      tablero[nuevaFilaVacia][nuevaColumnaVacia]);

                  /* variable que guarda valor que hay en la celda de destino */
                  int valorEnColumnaVacia = valorCasillaVacia[0];

                  /**
                   * se valida que la celda de destino cuando se ha eliminado una ficha de un
                   * oponente este disponible (0)
                   */
                  if (valorEnColumnaVacia == 0) {
                    /**
                     * se guarda la fila y la columna a donde se podria mover la ficha
                     * y se guarda la fila y la columna de la ficha del contrincante que se elimina
                     */
                    espaciosDisponibles.add(
                        nuevaFilaVacia +
                            "," +
                            nuevaColumnaVacia +
                            "," +
                            nuevaFila +
                            "," +
                            nuevaColumna);
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
   * 
   * @param fila
   * @param columna
   * @param tablero
   * @return
   */
  static ArrayList<String> PosibleCeldasAMover(int fila, int columna, String[][] tablero) {
    // fila = 0, columna = 1,
    /*
     * variable que va a guardar cuales son los espacios a los que se puede mover la
     * ficha
     * minimo 1, maximo 4, se ha utilizado un arrayList debido a que el tamaño es
     * dinamico
     */
    ArrayList<String> espaciosDisponibles = new ArrayList<String>();

    /* valoresFichas= contiene la info del tablero dadas las coordenadas */
    // fila = 0, columna = 1,
    // tablero[0][1] = "1,1,0"
    // valoresFicha = [1,1,0]
    /**
     * int[] valoresFicha = ConvertirStringAArray("1,1,0");
     * int[] valoresFicha = [1,1,0]
     */
    int[] valoresFicha = ConvertirStringAArray(tablero[fila][columna]);
    /*
     * [1, 1, 0] -> [numeroJugador, numeroFicha, esdama]
     */
    int numeroJugador = valoresFicha[0];
    int esDama = valoresFicha[2];

    /**
     * Se valida si no es una dama
     */
    if (esDama == 0) {
      /**
       * variable que indica el incremento que tendra la fila,
       * para el jugador 1 se aumenta
       * para el jugador 2 se disminuye
       */
      int incrementoFila = 0;

      if (numeroJugador == 1) {
        incrementoFila = 1;
      } else {
        incrementoFila = -1;
      }

      espaciosDisponibles = PosibleCeldaAMoverPorUsuario(
          tablero,
          numeroJugador,
          fila,
          columna,
          incrementoFila);
    } else {
      /**
       * como es una dama se evaluan las casillas de abajo y las casillas de arriba
       * cuando son casillas de abajo incrementoFila = 1
       * y cuando se validan las casillas de arriba incrementoFila = -1
       */
      ArrayList<String> espaciosAbajo = PosibleCeldaAMoverPorUsuario(
          tablero,
          numeroJugador,
          fila,
          columna,
          1);

      /**
       * se validan las casillas de arriba
       */
      ArrayList<String> espaciosArriba = PosibleCeldaAMoverPorUsuario(
          tablero,
          numeroJugador,
          fila,
          columna,
          -1);

      /**
       * se guarda las casillas disponibles que estan abajo en el arrayList de
       * espaciosDisponibles
       */
      for (int i = 0; i < espaciosAbajo.size(); i++) {
        espaciosDisponibles.add(espaciosAbajo.get(i));
      }

      /**
       * se guarda las casillas disponibles que estan arriba en el arrayList de
       * espaciosDisponibles
       */

      for (int i = 0; i < espaciosArriba.size(); i++) {
        espaciosDisponibles.add(espaciosArriba.get(i));
      }
    }

    return espaciosDisponibles;
  }

  /**
   * Valida que la coordenada dad este dentro del rango del tablero (1,8)
   * 
   * @param posicion
   * @return
   */
  static boolean PosicionRango(int posicion) {
    return posicion >= 1 && posicion <= 8;
  }

  /**
   * obtener de forma aleatoria que jugador va a empezar
   * 
   * @return
   */
  static int ObtenerTurnoInicial() {
    // https://es.stackoverflow.com/a/95079
    Random aleatorio = new Random();
    int eleccionTurno = aleatorio.nextInt(2 - 1 + 1) + 1;
    return eleccionTurno;
  }

  /**
   * Funcion que retorna el estado inicial del tablero
   * 
   * @return
   */
  static String[][] CrearTablero() {
    /**
     * Se declara la matriz del board y se establece su tamaño (8x8)
     */
    String tablero[][] = new String[8][8];

    /**
     * contador de fichas de cada jugador
     */
    int contadorJugador1 = 0;
    int contadorJugador2 = 0;

    /**
     * Datos que se guardan en el tablero
     * [0,0,0][1,1,0][0,0,0][1,2,0][0,0,0][1,3,0][0,0,0][1,4,0]
     * [1,5,0][0,0,0][1,6,0][0,0,0][1,7,0][0,0,0][1,8,0][0,0,0]
     * [0,0,0][1,9,0][0,0,0][1,10,0][0,0,0][1,11,0][0,0,0][1,12,0]
     * [0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0]
     * [0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0][0,0,0]
     * [2,1,0][0,0,0][2,2,0][0,0,0][2,3,0][0,0,0][2,4,0][0,0,0]
     * [0,0,0][2,5,0][0,0,0][2,6,0][0,0,0][2,7,0][0,0,0][2,8,0]
     * [2,9,0][0,0,0][2,10,0][0,0,0][2,11,0][0,0,0][2,12,0][0,0,0]
     */

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        // valida si la casilla esta vacia, si lo esta su valor = 0
        boolean esCasillaVacia = (filas + columnas) % 2 == 0;

        if (esCasillaVacia == true) {
          tablero[filas][columnas] = "0,0,0";
        } else {
          // && -> true && true -> true, false && true -> false
          // || -> false || true -> true, false || false -> false
          /**
           * Valida las filas en las cuales estaran las posiciones iniciales de las fichas
           */
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

  /**
   * funcion que dado un string separado por , se convierte a un array numerico
   */
  static int[] ConvertirStringAArray(String valorString) {
    // split: parte un string dado un caracter de separacion (,)
    /**
     * "1,1,0" -> ["1", "1", "0"]
     */
    String[] valores = valorString.split(",");
    /**
     * Se crea un array cuyo tamaño es igual al tamaño de valores, que se hace con
     * la funcion length
     */
    int[] resultado = new int[valores.length];

    /**
     * Se itera el valor de valores que es un string y se guarda cada valor en
     * resultado que es un entero
     * ["1", "1", "0"] -> [1,1,0]
     */
    for (int i = 0; i < valores.length; i++) {
      /*
       * Integer.parseInt -> convierte de string a numero
       */
      resultado[i] = Integer.parseInt(valores[i]);
    }

    return resultado;
  }

  /**
   * Funcion que imprime el tablero en la consola
   * 
   * @param tablero
   * @param turno
   */
  static void ImprimirTablero(
      String[][] tablero,
      int turno,
      int filaFichaSeleccionada,
      int columnaFichaSeleccionada,
      ArrayList<String> casillasALasQueSePuedeMover) {
    char muestraDama = '\u2654'; // Representa el símbolo de la dama

    limpiarPantalla();

    System.out.println();
    /**
     * Se obtiene la totalidad de fichas por jugador
     */
    int totalFichasJugador1 = TotalFichasPorJugador(tablero, 1);
    int totalFichasJugador2 = TotalFichasPorJugador(tablero, 2);

    /**
     * Muestra la cantidad de fichas de cada jugador
     */
    System.out.println(
        COLOR_ROJO + " ROJO: " + totalFichasJugador1 + " " + REINICIAR_COLOR);
    System.out.println(
        COLOR_AZUL + " AZUL: " + totalFichasJugador2 + " " + REINICIAR_COLOR);

    System.out.println();

    for (int filas = 0; filas < 8; filas++) {
      for (int columnas = 0; columnas < 8; columnas++) {
        /**
         * Se convierte el valor almacenado en la matriz de string a un array de numeros
         */
        int[] valoresFicha = ConvertirStringAArray(tablero[filas][columnas]);

        /**
         * se extrae cada uno de los valores
         */
        int numeroJugador = valoresFicha[0];
        int numeroFicha = valoresFicha[1];
        int esDama = valoresFicha[2];

        String muestraFicha = "";

        /**
         * Se establece que elemento se va a mostrar, si es dama se muestra la ficha
         * correspondiente, sino token
         */
        if (esDama == 1) {
          muestraFicha = " " + muestraDama + " ";
        } else {
          muestraFicha = " ◉ ";
        }

        /**
         * Valida si se muestra una casilla negra, si la suma de fila y columna es par
         */
        boolean esCasillaVacia = (filas + columnas) % 2 == 0;
        if (esCasillaVacia == true) {
          System.out.print("█████");
        } else {
          /**
           * Si el jugador es 1 o 2 se va a mostrar una ficha
           */
          if (numeroJugador == 1 || numeroJugador == 2) {
            String colorFicha = "";

            /**
             * se establece el color de ficha por jugador
             */
            if (numeroJugador == 1) {
              colorFicha = COLOR_ROJO;
            } else {
              colorFicha = COLOR_AZUL;
            }

            String valorCelda = "";

            /**
             * se valida si el numero de jugador es igual al turno actual
             * para asi poder mostrar sus posibles moviminetos
             */
            if (numeroJugador == turno) {
              /**
               * se valida si no hay ninguna ficha seleccionada por parte del usuario
               */
              if (filaFichaSeleccionada == -1) {
                /*
                 * evalua la cantidad de casillas que puede mover la ficha
                 * size = retorna el tamaño de un arrayList
                 */
                int totalDePosicionesAMover = PosibleCeldasAMover(
                    filas,
                    columnas,
                    tablero)
                    .size();
                /**
                 * si el total de posiciones a mover es == 0 muestra el tiken ya que no se puede
                 * mover
                 */
                if (totalDePosicionesAMover == 0) {
                  valorCelda = muestraFicha;
                } else {
                  // sino muestra el numero en el tablero para que el usuario sepa que puede
                  // seleccionarlo
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
                if (filas == filaFichaSeleccionada &&
                    columnas == columnaFichaSeleccionada) {
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
                colorFicha + " " + valorCelda + " " + REINICIAR_COLOR);
          } else {
            String celdaStringSeleccionada = "     ";
            /**
             * si filaseleccionada es diferente a -1 quiere decir que ya hay una ficha
             * seleccionada
             */
            if (filaFichaSeleccionada != -1) {
              for (int i = 0; i < casillasALasQueSePuedeMover.size(); i++) {
                /**
                 * se extrae las filas y las columnas de las posibles posiciones a las cuales se
                 * puede mover la ficha
                 */
                int[] posicionesEleccion = ConvertirStringAArray(
                    casillasALasQueSePuedeMover.get(i));

                int filaPosibleSeleccion = posicionesEleccion[0];
                int columnaPosibleSeleccion = posicionesEleccion[1];

                /**
                 * se valida si la fila y columna es igual a las posibles posiciones
                 */
                if (filas == filaPosibleSeleccion &&
                    columnas == columnaPosibleSeleccion) {
                  celdaStringSeleccionada = COLOR_AMARILLO + "  " + (i + 1) + "  " + REINICIAR_COLOR;
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
