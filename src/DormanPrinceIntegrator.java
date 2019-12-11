import java.util.*;

public class DormanPrinceIntegrator extends Integrator {

    public int counter;
    ArrayList<Double> list = new ArrayList<>();
    ArrayList<Double> listEps = new ArrayList<>();
    Matrix A = new Matrix(new double[][]{
        { 0. },
        { 1./5 },
        { 3./40, 9./40 },
        { 44./45, -56./15, 32./9 },
        { 19372./6561, -25360./2187, 64448./6561, -212./729 },
        { 9017./3168, -355./33, 46732./5247, 49./176, -5103./18656 },
        { 35./384, 0., 500./1113, 125./192, -2187./6784, 11./84 }
});
    Vector C = new Vector(new double[]{ 0, 1./5, 3./10, 4./5, 8./9, 1., 1. });
    Vector B = new Vector(new double[]{ 35./384, 0., 500./1113, 125./192, -2187./6784, 11./84, 0 });
    Vector B1 = new Vector(new double[]{ 5179./57600, 0., 7571./16695, 393./640, -92097./339200, 187./2100, 1./40 });
    Vector k;

    public long time = 0;

    @Override
    public void run (Model model) throws Exception
    {
        counter = 0;
        double h_new = model.h;
        double t_out;
        double epscur;
        Vector k1, k2, k3, k4, k5, k6, k7;
        Vector x;
        Vector sigx;
        Vector g = new Vector(new double[]{0,0,0,0,0,0});

        double v = 1;
        double u=0;
        while(1+v > 1)
        {
            u = v;
            v=v/2;
        }

        long sectime =0;
        double CH = model.h;
        double t = model.t0;
        double t1 = model.t0;
        while (t <= model.t1)
        {

            model.h = h_new;
            long ctime = System.currentTimeMillis();
            k1 = model.getRight(model.x0, t);


            // k2 = f(x0 + h*a21*k1, t + c2*h)
            k2 = model.getRight(model.x0.sum(k1.mult(model.h * A.data[1][0])),
                    t + C.data[1]*model.h);


            // k3 = f(x0 + h(a31*k1 + a32*k2, t+c3*h)
            Vector temp = k1.mult(model.h * A.data[2][0]).sum(k2.mult(model.h * A.data[2][1]));
            k3 = model.getRight(model.x0.sum(temp), t + model.h * C.data[2]);


            // k4 = f(x0 + h(a41*k1 + a42*k2 + a43*k3), t + c4*h)
            temp = k1.mult(model.h * A.data[3][0]);
            temp = temp.sum(k2.mult(model.h * A.data[3][1]));
            temp = temp.sum(k3.mult(model.h * A.data[3][2]));
            k4 = model.getRight(model.x0.sum(temp), t+ model.h*C.data[3]);


            temp = k1.mult(model.h * A.data[4][0]);
            temp = temp.sum(k2.mult(model.h * A.data[4][1]));
            temp = temp.sum(k3.mult(model.h * A.data[4][2]));
            temp = temp.sum(k4.mult(model.h * A.data[4][3]));
            k5 = model.getRight(model.x0.sum(temp), t + model.h*C.data[4]);


            temp = k1.mult(model.h * A.data[5][0]);
            temp = temp.sum(k2.mult((model.h * A.data[5][1])));
            temp = temp.sum(k3.mult(model.h * A.data[5][2]));
            temp = temp.sum(k4.mult(model.h * A.data[5][3]));
            temp = temp.sum(k5.mult(model.h * A.data[5][4]));
            k6 = model.getRight(model.x0.sum(temp), t + model.h*C.data[5]);


            temp = k1.mult(model.h * A.data[6][0]);
            temp = temp.sum(k2.mult((model.h * A.data[6][1])));
            temp = temp.sum(k3.mult(model.h * A.data[6][2]));
            temp = temp.sum(k4.mult(model.h * A.data[6][3]));
            temp = temp.sum(k5.mult(model.h * A.data[6][4]));
            temp = temp.sum(k6.mult(model.h * A.data[6][5]));
            k7 = model.getRight(model.x0.sum(temp), t + model.h*C.data[6]);

            //x1 = x0 + h(b1*k1 + b2*k2 + b3*k3 + b4*k4)
            temp = k1.mult(B.data[0]);
            temp = temp.sum(k2.mult(B.data[1]));
            temp = temp.sum(k3.mult(B.data[2]));
            temp = temp.sum(k4.mult(B.data[3]));
            temp = temp.sum(k5.mult(B.data[4]));
            temp = temp.sum(k6.mult(B.data[5]));
            x = model.x0.sum(temp.mult(model.h));

            temp = k1.mult(B1.data[0]);
            temp = temp.sum(k2.mult(B1.data[1]));
            temp = temp.sum(k3.mult(B1.data[2]));
            temp = temp.sum(k4.mult(B1.data[3]));
            temp = temp.sum(k5.mult(B1.data[4]));
            temp = temp.sum(k6.mult(B1.data[5]));
            temp = temp.sum(k7.mult(B1.data[6]));
            sigx = model.x0.sum(temp.mult(model.h));

            time += System.currentTimeMillis() - ctime;


            double sum = 0;
            double a = Math.max(Math.pow(10, -5), 2*u/epsmax);
            for (int i = 0; i < model.x0.data.length; i++) {
                double b = Math.max(Math.abs(model.x0.data[i]), Math.abs(x.data[i]));
                sum += Math.pow(model.h * Math.abs((x.data[i] - sigx.data[i])) /
                        (Math.max(a,b)), 2);
            }
            sum /= model.x0.data.length;
            double eps = Math.pow(sum, 0.5);
            listEps.add(eps);


            h_new = model.h / Math.max(0.1, (Math.min(5., Math.pow(eps/epsmax, 1./5)/0.9) ));
            list.add(model.h);
            if (eps > epsmax)
            {
                continue;
            }


            if(t1<t+model.h)
            {
                Vector xt1 = new Vector(new double [model.x0.data.length]);
                Vector tmp = new Vector(new double[model.x0.data.length]);
                while(t1<t + model.h)
                {
                    double theta = (t1 - t) / model.h;
                    g.data[0] = theta * (1 + theta*(-1337./480 + theta*(1039./360 + theta*(-1163./1152))));
                    g.data[1] = 0;
                    g.data[2] = 100*theta*theta*(1054./9275 + theta*(-4682./27825 + theta*(379./5565)))/3;
                    g.data[3] = -5*theta*theta*(27./40 + theta*(-9./5 + theta*(83./96)))/2;
                    g.data[4] = 18225*theta*theta*(-3./250 + theta*(22./375 + theta*(-37./600)))/848;
                    g.data[5] = -22*theta*theta*(-3./10 + theta*(29./30 + theta*(-17./24)))/7;
                    tmp = k1.mult(g.data[0]);
                    tmp = tmp.sum(k2.mult(g.data[1]));
                    tmp = tmp.sum(k3.mult(g.data[2]));
                    tmp = tmp.sum(k4.mult(g.data[3]));
                    tmp = tmp.sum(k5.mult(g.data[4]));
                    tmp = tmp.sum(k6.mult(g.data[5]));
                    t1+=CH;
                }
                xt1 = model.x0.sum(tmp.mult(CH));
                model.addResult(xt1, t1);
            }

            model.x0 = x;
            sectime += System.currentTimeMillis() - ctime;
            t+=model.h;
        }

    }


}
