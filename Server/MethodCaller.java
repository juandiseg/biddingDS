public class MethodCaller implements java.io.Serializable {

    private String managerName;
    private String methodName;
    private Object[] args;

    public MethodCaller(String managerName, String methodName, Object[] args) {
        this.managerName = managerName;
        this.methodName = methodName;
        this.args = args;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

}
