package pl.edu.pw.mini.msi.diagnoser;

public class Test {

    public static double[][] DIAGNOSIS_SYMPTOMS = {
            {0.4, 0.3, 0.1, 0.4, 0.1},
            {0.7, 0.1, 0.0, 0.7, 0.1},
            {0.3, 0.6, 0.2, 0.2, 0.1},
            {0.1, 0.2, 0.8, 0.2, 0.2},
            {0.1, 0.0, 0.2, 0.2, 0.8}
    };

    public static double[][] PATIENTS_SYMPTOMS = {
            {0.8, 0.6, 0.2, 0.6, 0.1},
            {0.0, 0.4, 0.6, 0.1, 0.1},
            {0.8, 0.8, 0.0, 0.2, 0.0},
            {0.6, 0.5, 0.3, 0.7, 0.3},
    };

    public static void main(String[] args) {
        double[][][] approx_patient_symptoms =
                approx_characteristics(DIAGNOSIS_SYMPTOMS, PATIENTS_SYMPTOMS);
        double[][][] approx_diagnosis_symptoms =
                approx_characteristics(DIAGNOSIS_SYMPTOMS, DIAGNOSIS_SYMPTOMS);

        double[][] distances_hamming =
                calculate_distances(
                        approx_patient_symptoms,
                        approx_diagnosis_symptoms,
                        Test::normalized_hamming);

        double[][] distances_euclidean =
                calculate_distances(
                        approx_patient_symptoms,
                        approx_diagnosis_symptoms,
                        Test::normalized_euclidean);
    }

    public static double[][] calculate_distances(double[][][] a, double[][][] b, QuadFunction<double[], double[], double[], double[], Double> function) {
        double[][] distances = new double[a.length][b.length];
        for (int i = 0; i < PATIENTS_SYMPTOMS.length; i++) {
            for (int j = 0; j < DIAGNOSIS_SYMPTOMS.length; j++) {
                distances[i][j] = function.apply(
                        a[i][0],
                        a[i][1],
                        b[j][0],
                        b[j][1]);
                System.out.printf("%.3f ", distances[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        return distances;
    }

    public static double[][][] approx_characteristics(double[][] R, double[][] Q) {
        double[][][] approx = new double[Q.length][2][];
        for(int i = 0; i < Q.length; i++) {
            double[] q = Q[i];
            approx[i][0] = lower_bound(R, q);
            approx[i][1] = upper_bound(R, q);
            for (int j = 0; j < q.length; j++) {
                System.out.printf("%.2f/%.2f ", approx[i][0][j], approx[i][1][j]);
            }
            System.out.println();
        }
        System.out.println();
        return approx;
    }

    public static double[] lower_bound(double[][] R, double[] A) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            double sup = - Double.MAX_VALUE;
            for(int x = 0; x < A.length; x++) {
                double inf = Double.MAX_VALUE;
                for(int y = 0; y < A.length; y++) {
                    double min = Math.min(1, 1 - R[x][y] + A[y]);
                    if(min < inf) {
                        inf = min;
                    }
                }
                double max = Math.max(0, R[x][i] + inf - 1);
                if(max > sup) {
                    sup = max;
                }
            }
            result[i] = sup;
        }
        return result;
    }

    public static double[] upper_bound(double[][] R, double[] A) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            double inf = Double.MAX_VALUE;
            for(int x = 0; x < A.length; x++) {
                double sup = - Double.MAX_VALUE;
                for(int y = 0; y < A.length; y++) {
                    double max = Math.max(0, R[x][y] + A[y] - 1);
                    if(max > sup) {
                        sup = max;
                    }
                }
                double min = Math.min(1, 1 - R[x][i] + sup);
                if(min < inf) {
                    inf = min;
                }
            }
            result[i] = inf;
        }
        return result;
    }

    public static double normalized_hamming(double[] x_l, double[] x_u, double[] y_l, double[] y_u) {
        double result = 0;
        for (int i = 0; i < x_l.length; i++) {
            result +=
                    Math.abs(x_u[i] - y_u[i]) +
                    Math.abs(x_l[i] - y_l[i]) +
                    Math.abs(x_l[i] - x_u[i] - y_l[i] + y_u[i]) ;
        }
        result /= 2 * x_l.length;
        return result;
    }

    public static double normalized_euclidean(double[] x_l, double[] x_u, double[] y_l, double[] y_u) {
        double result = 0;
        for (int i = 0; i < x_l.length; i++) {
            result +=
                    (x_u[i] - y_u[i]) * (x_u[i] - y_u[i]) +
                            (x_l[i] - y_l[i]) * (x_l[i] - y_l[i]) +
                            (x_l[i] - x_u[i] - y_l[i] + y_u[i]) * (x_l[i] - x_u[i] - y_l[i] + y_u[i]) ;
        }
        result /= 2 * x_l.length;
        return result;
    }

    @FunctionalInterface
    public interface QuadFunction<U, V, W, X, R> {
        R apply(U u, V v, W w, X x);
    }

}
