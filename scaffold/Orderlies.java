public class Orderlies {
    protected volatile int numberOfOrderlies = Params.ORDERLIES;

    public synchronized int releaseOrderlies(){
        numberOfOrderlies += Params.TRANSFER_ORDERLIES;
        return numberOfOrderlies;
    }

    public synchronized int recruitOrderlies(){
        numberOfOrderlies -= Params.TRANSFER_ORDERLIES;
        return numberOfOrderlies;
    }
}
