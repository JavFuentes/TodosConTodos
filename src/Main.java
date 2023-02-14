import java.util.Scanner;

public class Main {
	public static float tiempoRestante;
	public static int numeroAlumnos = 0;
	public static final float tiempoIntercambio = 30;
	public static int listaAlumnos[];
	public static int alumnoA[], alumnoB[], alumnoC[], alumnoD[];
	public static int parejasPosibles = 0;
	public static int rondas = 0;
	public static boolean alumnosPares;
	public static int paresPorRonda = 0;
	public static int contadorPosiciones = 0;
	public static int saltoRonda;
	public static int contadorMultiuso = 0;
	public static int tiempoTotalIntercambio;
	public static int intercambios;
	public static int tiempoPorRonda;

	public static void main(String[] args) {

		leerDatos();
		
		validarDatos();

		// Título del programa
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

		calcularTiempo();

		imprimirResultadoFinal();

	}

	static void completarArreglosDeOrdenacion() {
		// Algoritmo Principal
		Siguiente: for (int i = 0; i < alumnoA.length; i++) {
			for (int j = 0; j < alumnoA.length; j++) {
				if ((alumnoA[i] == alumnoC[j]) && (alumnoB[i] == alumnoD[j])) {
					// TESTING
					// System.out.println("(" + alumnoA[i] + ")(" + alumnoB[i] + ")");

					continue Siguiente;

				} else {
					if (j == alumnoA.length - 1) {
						// TESTING
						// System.out.println("(x)(x)");

						// Algoritmo Secundario
						BuscarEnCadaRonda: for (int k = 0; k < rondas; k++) {
							for (int l = 0; l < paresPorRonda - 1; l++) {

								saltoRonda = paresPorRonda * k;
								if (alumnoA[i] == alumnoC[l + saltoRonda] || alumnoB[i] == alumnoD[l + saltoRonda]
										|| alumnoA[i] == alumnoD[l + saltoRonda]
										|| alumnoB[i] == alumnoC[l + saltoRonda]) {

									continue BuscarEnCadaRonda;

								} else {
									if (l == paresPorRonda - 2) {

										// Algoritmo Terciario
										for (int m = 0; m < paresPorRonda; m++) {
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
		// Si el numero de alumnos es impar ira ubicando en cada ronda un alumno sin
		// pareja distinto
		if (!alumnosPares) {
			for (int i = alumnoA.length - 1; i >= 0; i--) {
				if (alumnoA[i] == 0 || alumnoB[i] == 0) {
					alumnoC[contadorPosiciones] = alumnoA[i];
					alumnoD[contadorPosiciones] = alumnoB[i];
					contadorPosiciones = contadorPosiciones + paresPorRonda;
				}
			}
			contadorPosiciones = 0;
		}

		// Si el número de alumnos es par ira ubicando todas las combinaciones que
		// incluyen el 1 en una ronda distinta
		if (alumnosPares) {
			for (int i = 0; i < alumnoA.length; i++) {
				if (alumnoA[i] == 1 || alumnoB[i] == 1) {
					alumnoC[contadorPosiciones] = alumnoA[i];
					alumnoD[contadorPosiciones] = alumnoB[i];
					contadorPosiciones = contadorPosiciones + paresPorRonda;
				}
			}
			contadorPosiciones = 0;
		}
	}
}
