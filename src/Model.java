import java.util.Arrays;
import java.util.List;

public abstract class Model {
    List<Matrix> disp;
    Matrix result;
    double h; // sampleIncrement
    double t0, t1;
    Vector x0;


    public void addResult(Vector vec, double k)
    {
        double[][] temp = new double[result.data.length][result.data[0].length+1];
        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[0].length; j++) {
                temp[i][j] = result.data[i][j];
            }
        }
        result.data = temp;
        int lastCol = result.data[0].length-1;
        for (int i = 0; i <= result.data.length-1; i++) {
            result.data[i][lastCol] = vec.data[i];
        }
    }
    public void AddResultMatrix(Matrix m, double k)
    {
        disp.add(m);
    }

    public Model(double t0, double t1, double h)
    {
        this.t0 = t0;
        this.t1 = t1;
        this.h = h;
    }

    public Vector getInitialCondition()
    {
        return x0;
    }
    public void setInitialCondition(Vector x0)
    {
        this.x0 = x0;
    }

    public Matrix getResult()
    {
        return result;
    }

    public Vector getRight(Vector vec, double t)
    {
        return null;
    }

    public double getSampleIncrement()
    {
        return h;
    }

    public double getT0() {
        return t0;
    }

    public double getT1() {
        return t1;
    }

}

class MathModel1 extends Model
{

    public MathModel1(double t0, double t1, double h)
    {
        super(t0, t1, h);
        result = new Matrix(new double[][]{
                {0.01}
        });
    }

    @Override
    public Vector getRight(Vector vec, double t)
    {
        return new Vector(new double[]{-5*Math.pow(t, 3)*vec.data[0]});
    }
}

class Kamen extends Model
{
    public Kamen(double t0, double t1, double h)
    {
        super(t0, t1, h);
        result = new Matrix(new double[][]{
                {0},
                {50},
                {5},
                {100}
        });
        x0 = new Vector(new double[] {0, 50, 5, 100});
    }
    @Override
    public Vector getRight(Vector vec, double t)
    {
        double po = 1.12;
        double S = 1;
        double g = 9.81;
        double m = 2;
        return new Vector(new double[]
                {vec.data[1], -Math.pow(vec.data[1], 2)*S*po/(2*g*m),
                vec.data[3], -g-Math.pow(vec.data[3], 2)*S*po/(2*g*m) } );
    }
}