import java.util.Random;
import java.util.Scanner;

/**
 * @author JavFuentes
 *
 */
public class Main {
	
	static float tiempoRestante;
	static int numeroAlumnos = 0;
	static final float tiempoIntercambio = 30;
	static int listaAlumnos[];
	static int alumnoA[], alumnoB[], alumnoAa[], alumnoBb[], alumnoC[], alumnoD[];
	static int parejasPosibles = 0;
	static int rondas = 0;
	static boolean alumnosPares;
	static int paresPorRonda = 0;
	static int contadorPosiciones = 0;
	static int saltoRonda;
	static int contadorMultiuso = 0;
	static int tiempoTotalIntercambio;
	static int intercambios;
	static int tiempoPorRonda;
	static boolean problemaResuelto;
	
	public static void main(String[] args) {

		leerDatos();
		
		validarDatos();

		//Título del programa
		System.out.println("-------------------");
		System.out.println("- Todos Con Todos -");
		System.out.println("-------------------");

		definirNumeroDeRondasEIntermedios();

		rellenarListaAlumnos();

		imprimirParejasPosibles();

		definirArreglosDeOrdenacion();
		
		rellenarArreglosConParejasPosibles();

		determinarParesPorRonda();

		agregarUnaParejaExcluyentePorRonda();

		completarArreglosDeOrdenacion();
		problemaResuelto = true;
		
		if(comprobarSiEstaResuelto()) {
			System.out.println("1111");
			calcularTiempo();
			imprimirResultadoFinal();
		} else {
			problemaResuelto = true;
			while(!comprobarSiEstaResuelto()) {
				completarOrdenacionPorFuerzaBruta();				
			}	
			System.out.println("2222");
			calcularTiempo();			
			imprimirResultadoFinal();
		}

		
		
	
				
	}

	static void completarArreglosDeOrdenacion() {
		
		/*
		 * [Algoritmo Primario] 
		 * Se comprueba si cada pareja de AB se encuentra en CD,
		 * hasta que lo encuentra y vuelve a AB por otra pareja.
		 * Si no lo encuentra en todo CD, se intentará ubicar con el 
		 * algoritmo secundario. 
		 */
		Siguiente: 
		for (int i = 0; i < alumnoA.length; i++) {
			for (int j = 0; j < alumnoB.length; j++) {
				if ((alumnoA[i] == alumnoC[j]) && (alumnoB[i] == alumnoD[j])) {	
					//Si lo encuentra volvemos a AB por otra pareja.
					continue Siguiente;
				} else {
					//Si ya se intentó agregar en cada posición de CD...
					if (j == alumnoC.length - 1) {						

						/*
						 * [Algoritmo Secundario] 
						 * Este algoritmo intenta ubicar en CD a las parejas restantes
						 */
						BuscarEnCadaRonda: 
						for (int k = 0; k < rondas; k++) {
							for (int l = 0; l < paresPorRonda - 1; l++) {
								
								saltoRonda = paresPorRonda * k;
								if (alumnoA[i] == alumnoC[l + saltoRonda] || alumnoB[i] == alumnoD[l + saltoRonda]
										|| alumnoA[i] == alumnoD[l + saltoRonda]
										|| alumnoB[i] == alumnoC[l + saltoRonda]) {

									continue BuscarEnCadaRonda;

								} else {
									//?
									if (l == paresPorRonda - 2) {
										
										/*
										 * [Algoritmo Terciario] 
										 * Este algoritmo ubicará en CD a las parejas restantes
										 */
										for (int m = 0; m < paresPorRonda; m++) {
											//Se comprueba que la ubicación corresponda a un 0,0
											if (alumnoC[m + saltoRonda] == alumnoD[m + saltoRonda]) {
												alumnoC[m + saltoRonda] = alumnoA[i];
												alumnoD[m + saltoRonda] = alumnoB[i];
												continue Siguiente;
											}
										}
									}
								}
							}
						}
						continue Siguiente;
					}
				}
			}
		}
	}

	static void calcularTiempo() {
		tiempoTotalIntercambio = (int) tiempoIntercambio * intercambios;
		System.out.println("Intercambios: " + intercambios + ".\nTiempo total en intercambios: "
				+ (tiempoTotalIntercambio / 60) + " minutos.");
		tiempoRestante = tiempoRestante - tiempoTotalIntercambio;
		tiempoPorRonda = (int) tiempoRestante / rondas;
	}

	static void imprimirResultadoFinal() {
		System.out.println("----------------------------------");
		System.out.println("Ronda 1" + "\n(" + tiempoPorRonda / 60 + " minutos)");
		for (int i = 0; i < alumnoC.length; i++) {
			if (contadorMultiuso == paresPorRonda) {
				System.out.println("\n");
				System.out.println("Ronda " + ((i / paresPorRonda) + 1) + "\n(" + tiempoPorRonda / 60 + " minutos)");
				contadorMultiuso = 0;
			}
			System.out.println("[" + alumnoC[i] + "][" + alumnoD[i] + "]");
			contadorMultiuso++;
		}
		System.out.println();
	}

	static void validarDatos() {
		while ((tiempoRestante / 60) / numeroAlumnos < 8) {
			System.out.println("[ERROR]");
			System.out.println(
					"Imposible. La cantidad de alumnos vs el tiempo no permite que la actividad se realice correctamente.");
			System.out.println("Se requiere mas tiempo o disminuir el numero de alumnos.\n");

			leerDatos();
		}
	}

	static void leerDatos() {
		Scanner sc = new Scanner(System.in);

		// Se pide ingresar por consola el número total de alumnos
		System.out.println("Ingrese el numero de alumnos: ");
		numeroAlumnos = sc.nextInt();

		// Se multiplica por 60 para convertir a segundos
		System.out.println("Ingrese el tiempo restante en minutos: ");
		tiempoRestante = sc.nextInt() * 60;
	}

	// Se define el número de rondas en base a si el numero de alumnos es par o no.
	static void definirNumeroDeRondasEIntermedios() {
		if (numeroAlumnos % 2 == 0) {
			rondas = numeroAlumnos - 1;
			alumnosPares = true;
		} else {
			rondas = numeroAlumnos;
			alumnosPares = false;
		}
		// La cantidad de tiempos intermedios es igual al números intermedios menos 1
		intercambios = rondas - 1;
	}

	static void rellenarListaAlumnos() {
		// Se define el largo de la lista de alumnos
		if (alumnosPares) {
			listaAlumnos = new int[numeroAlumnos];
		} else {
			listaAlumnos = new int[numeroAlumnos + 1];
		}

		// El arreglo se rellena con la totalidad de los alumnos, identificados con un
		// número
		for (int i = 0; i < numeroAlumnos; i++) {
			listaAlumnos[i] = i + 1;
		}
	}

	static void imprimirParejasPosibles() {		
		// Imprime todas las parejas únicas posibles y las cuenta
		for (int i = 0; i < listaAlumnos.length; i++) {
			for (int j = i + 1; j < listaAlumnos.length; j++) {
				System.out.println("[" + listaAlumnos[i] + "][" + listaAlumnos[j] + "]");
				parejasPosibles++;
			}
		}
		// Imprime la cantidad de pares únicos posibles
		System.out.println("Parejas unicas posibles: " + parejasPosibles + ".");
	}

	static void definirArreglosDeOrdenacion() {
		// Se define el largo del primer par de arreglos que contienen todas las
		// convinaciones únicas posibles
		alumnoA = new int[parejasPosibles];
		alumnoB = new int[parejasPosibles];

		// De la misma forma se define el largo que contendrá los alumnos ordenados por
		// rondas
		alumnoC = new int[parejasPosibles];
		alumnoD = new int[parejasPosibles];
		
		//Se define el largo de los arreglos que contienen una pareja excluyente por ronda
		alumnoAa = new int[parejasPosibles];
		alumnoBb = new int[parejasPosibles];
	}

	static void rellenarArreglosConParejasPosibles() {
		// Rellenamos los arreglos que contienen todas las convinaciones únicas posibles
		for (int i = 0; i < listaAlumnos.length; i++) {
			for (int j = i + 1; j < listaAlumnos.length; j++) {
				alumnoA[contadorMultiuso] = listaAlumnos[i];
				alumnoB[contadorMultiuso] = listaAlumnos[j];
				contadorMultiuso++;
			}
		}
		contadorMultiuso = 0;
	}

	static void determinarParesPorRonda() {
		// Determina cuantos pares de alumnos hay en cada ronda
		if (alumnosPares) {
			paresPorRonda = numeroAlumnos / 2;
		} else {
			paresPorRonda = numeroAlumnos / 2 + 1;
		}
	}

	static void agregarUnaParejaExcluyentePorRonda() {
		// Si el numero de alumnos es impar, se irá ubicando en cada ronda un alumno sin
		// pareja distinto.
		if (!alumnosPares) {
			for (int i = alumnoA.length - 1; i >= 0; i--) {
				if (alumnoA[i] == 0 || alumnoB[i] == 0) {
					alumnoC[contadorPosiciones] = alumnoA[i];
					alumnoD[contadorPosiciones] = alumnoB[i];
					
					alumnoAa[contadorPosiciones] = alumnoA[i];
					alumnoBb[contadorPosiciones] = alumnoB[i];
					
					contadorPosiciones = contadorPosiciones + paresPorRonda;
				}
			}
			contadorPosiciones = 0;
		}

		// Si el número de alumnos es par, se irá ubicando todas las combinaciones que
		// incluyen el 1 en una ronda distinta.
		if (alumnosPares) {
			for (int i = 0; i < alumnoA.length; i++) {
				if (alumnoA[i] == 1 || alumnoB[i] == 1) {
					alumnoC[contadorPosiciones] = alumnoA[i];
					alumnoD[contadorPosiciones] = alumnoB[i];
					
					alumnoAa[contadorPosiciones] = alumnoA[i];
					alumnoBb[contadorPosiciones] = alumnoB[i];
					
					contadorPosiciones = contadorPosiciones + paresPorRonda;
				}
			}
			contadorPosiciones = 0;
		}
	}
	
	static void completarOrdenacionPorFuerzaBruta() {
		//Se establece un arreglo de tiene un largo igual al numero de rondas
		//Cada vez que se pruebe con una ronda distinta, esta quedará marcada,
		//De esta forma el tiempo de búsqueda se acortará, ya que no probará 
		//más de una vez con la misma ronda.
		boolean[] arregloRondas = new boolean[rondas];	
		int nuevoIntento = 0;
		int intentos = 0;
		
		
		//DC vuelve al punto donde solo existía una PEPR
		probarNuevaCombinacion();
		
		
		/*
		 * [Algoritmo Primario] 
		 * Se comprueba si cada pareja de AB se encuentra en CD,
		 * hasta que lo encuentra y vuelve a AB por otra pareja.
		 * Si no lo encuentra en todo CD, se intentará ubicar con el 
		 * algoritmo secundario. 
		 */
		Siguiente: 
		for (int i = 0; i < alumnoA.length; i++) {
			for (int j = 0; j < alumnoB.length; j++) {				
				if ((alumnoA[i] == alumnoC[j]) && (alumnoB[i] == alumnoD[j])) {						
					//Si lo encuentra volvemos a AB por otra pareja.
					continue Siguiente;
				} else {
					//Si ya comprobo con cada posicion de CD y no lo encontró, entonces intentará ubicarlo
					if (j == alumnoC.length - 1) {						

						/*
						 * [Algoritmo Secundario] 
						 * Intentará ubicar la pareja en una ronda aleatoria de CD.
						 */
												
						//Se reinicia el arreglo de marcado de rondas probadas.
						for(int n = 0; n < rondas; n++) {
							arregloRondas[n] = false;
						}
						
						//Mientras no se haya comprobado con todas las rondas posibles...
						BuscarEnCadaRonda: 
						while(intentos < rondas+1) {
							//Se intentará con una ronda al azar.
							nuevoIntento = rondaAleatoria();
							
							//Se comprueba si ya se intentó con esa ronda:
							if(arregloRondas[nuevoIntento] == false) {
								intentos++;
								arregloRondas[nuevoIntento] = true;
								
								for (int l = 0; l < paresPorRonda - 1; l++) {
									saltoRonda = paresPorRonda * nuevoIntento;
									if (alumnoA[i] == alumnoC[l + saltoRonda] || alumnoB[i] == alumnoD[l + saltoRonda]
											|| alumnoA[i] == alumnoD[l + saltoRonda]
											|| alumnoB[i] == alumnoC[l + saltoRonda]) {

										continue BuscarEnCadaRonda;

									} else {
										
										if (l == paresPorRonda - 2) {
											
											/*
											 * [Algoritmo Terciario] 
											 * Este algoritmo ubicará en CD a las parejas restantes
											 */
											for (int m = 0; m < paresPorRonda; m++) {
												//Se comprueba que la ubicación corresponda a un 0,0
												if (alumnoC[m + saltoRonda] == alumnoD[m + saltoRonda]) {
													alumnoC[m + saltoRonda] = alumnoA[i];
													alumnoD[m + saltoRonda] = alumnoB[i];
													continue Siguiente;
												}
											}
										}
									}									
								}
							} else {
								continue BuscarEnCadaRonda;
							}
						}	
						calcularTiempo();			
						imprimirResultadoFinal();
						
						probarNuevaCombinacion();
						intentos = 0;
						System.out.println("lol");
												
						continue Siguiente;
					}
				}
			}
		}		
	}
	
	static int rondaAleatoria() {
		int rondaAleatoria = 0;
		Random random = new Random();	
		rondaAleatoria =  random.nextInt(rondas);
		System.out.println(rondaAleatoria);
		return rondaAleatoria;
	}
	
	static void probarNuevaCombinacion() {
		for(int i = 0; i < alumnoA.length; i++) {				
			alumnoC[i] = alumnoAa[i];
			alumnoD[i] = alumnoBb[i];		
		}
	}
	
	static boolean comprobarSiEstaResuelto() {
		for (int i = 0; i < alumnoA.length; i++) {
			if(alumnoC[i] == 0 && alumnoD[i] == 0) {
				problemaResuelto = false;
			}
		}
		return problemaResuelto;
	}
}
