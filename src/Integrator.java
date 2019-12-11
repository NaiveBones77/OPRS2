

public class Integrator {

    double epsmax = 1e-18;
    double zero;
    Matrix A = new Matrix(new double[][]{
            {0,0,0,0},
            {0.2, 0, 0, 0},
            {0.075, 0.225, 0 , 0},
            {0.977777, -3.733333, 3.555555,  0}
    });
    Vector C = new Vector(new double[]{0.2, 0.3, 0.8, 0.88888});
    Vector B = new Vector(new double[]{0.091145833, 0, 0.449236298292, 0.65104166666667});

    public Integrator(double eps)
    {
        this.epsmax = eps;
    }

    public Integrator()
    {

    }

    public void create()
    {

    }

    public void run(Model model) throws Exception
    {

        Vector k1, k2, k3, k4;
        Vector x = new Vector();

        double t = model.t0;
        while (t <= model.t1)
        {
            k1 = model.getRight(model.x0, t);


            // k2 = f(x0 + h*a21*k1, t + c2*h)
            k2 = model.getRight(model.x0.sum(k1.mult(model.h * A.data[1][0])),
                    t + C.data[0]*model.h);


            // k3 = f(x0 + h(a31*k1 + a32*k2, t+c3*h)
            Vector temp = k1.mult(model.h * A.data[2][0]).sum(k2.mult(model.h * A.data[2][1]));
            k3 = model.getRight(model.x0.sum(temp), t + model.h * C.data[1]);


            // k4 = f(x0 + h(a41*k1 + a42*k2 + a43*k3), t + c4*h)
            temp = k1.mult(model.h * A.data[3][0]);
            temp = temp.sum(k2.mult(model.h * A.data[3][1]));
            temp = temp.sum(k3.mult(model.h * A.data[3][2]));
            k4 = model.getRight(model.x0.sum(temp), t+ model.h*C.data[2]);


            //x1 = x0 + h(b1*k1 + b2*k2 + b3*k3 + b4*k4)
            temp = k1.mult(B.data[0]);
            temp = temp.sum(k2.mult(B.data[1]));
            temp = temp.sum(k3.mult(B.data[2]));
            temp = temp.sum(k4.mult(B.data[3]));
            x = model.x0.sum(temp.mult(model.h));

            model.x0 = x;
            model.addResult(x, t);
            t+=model.h;
        }
    }

    public void setPrecision(double eps)
    {
        this.epsmax = eps;
    }

}
