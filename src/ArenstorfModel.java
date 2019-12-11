
class ArenstorfModel extends Model {


    double D1, D2;
    double mu = 0.012277471;
    double altmu = 1 - mu;

    public ArenstorfModel(double t0, double t1, double h, Vector x0)
    {
        super(t0, t1, h);
        this.addResult(x0, t0);
    }

    public ArenstorfModel(double t0, double t1, double h)
    {
        super(t0, t1, h);
        result = new Matrix(new double[][]{
                {0.994},
                {0},
                {0},
                {-2.0317326295573268357302057924}
        });
    }

    @Override
    public Vector getRight(Vector vec, double t) {
        //Вектор входных параметров имеет вид vec = [ y1, y1', y2, y2' ]
        //Возвращаем вектор результата расчета системы из 4 ДУ 1-го порядка
        // result = [ y1'', y2'', y1', y2' ]
        D1 =Math.pow(Math.pow(vec.data[0] + mu, 2) + Math.pow(vec.data[2], 2), 1.5);
        D2 =Math.pow(Math.pow(vec.data[0] - altmu, 2) + Math.pow(vec.data[2], 2), 1.5);

        Vector result = new Vector(new double[4]);
        result.data[1]=vec.data[0] + 2*vec.data[3] - altmu*((vec.data[0] + mu) / D1 )
                - mu*((vec.data[0] - altmu) / D2);
        result.data[3] = vec.data[2] - 2*vec.data[1] - altmu*(vec.data[2] / D1)
                -mu*(vec.data[2] / D2);
        result.data[0] = vec.data[1];
        result.data[2] = vec.data[3];
        return result;
    }
}
