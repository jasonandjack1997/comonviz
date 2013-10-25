package test;
public class Calculator {
    private static int result; // �?��?�?��?，用于存储�?行结果
    public void add(int n) {
        result = result + n;
        System.out.println("hello add");
 
    }
    public void substract(int n) {
        result = result - 1;  //Bug: 正确的应该是 result =result-n
    }
    public void multiply(int n) {
    }         // 此方法尚未写好
    public void divide(int n) {
        result = result / n;
    }
    public void square(int n) {
        result = n * n;
    }
    public void squareRoot(int n) {
        for (; ;) ;            //Bug : 死循环
    }
    public void clear() {     // 将结果清零
        result = 0;
    }
    public int getResult() {
        return result;
    }
}