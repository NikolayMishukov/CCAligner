
public class Example{
    public void foo(int x, int y){
        x = x + y;
        y = x - y;
        if(x + y > 0) {
            System.out.println(x);
        }
        else{
            System.out.println(y);
        }
    }

    public void bar(int x){
        for (int i = 0; i < 10; ++i){
            if(i > x){
                System.out.println(i+x);
            }
            else{
                System.out.println("Wow");
            }
        }
    }
}

public class Clone{
    public void cloneFunction(int a, int b){
        a = a +          b;

        b = a
                -
                b;
        if(a + b > 0) { System.out.println(a);}
        else{System.out.println(b);}
        System.out.println("Not exactly clone");
    }
}
