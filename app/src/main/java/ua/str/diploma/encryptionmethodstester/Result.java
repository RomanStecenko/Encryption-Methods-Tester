package ua.str.diploma.encryptionmethodstester;

public class Result {
    private int _id;
    private String method;
    private long encryptionTime;
    private long decryptionTime;
    private long encryptedFileSize;
    private long cpuRate;
    private long timestamp;

    public Result() {
    }

    public Result(String method, long encryptionTime, long encryptedFileSize, long timestamp) {
        this.method = method;
        this.encryptionTime = encryptionTime;
        this.encryptedFileSize = encryptedFileSize;
        this.timestamp = timestamp;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getEncryptionTime() {
        return encryptionTime;
    }

    public void setEncryptionTime(long encryptionTime) {
        this.encryptionTime = encryptionTime;
    }

    public long getDecryptionTime() {
        return decryptionTime;
    }

    public void setDecryptionTime(long decryptionTime) {
        this.decryptionTime = decryptionTime;
    }

    public long getEncryptedFileSize() {
        return encryptedFileSize;
    }

    public void setEncryptedFileSize(long encryptedFileSize) {
        this.encryptedFileSize = encryptedFileSize;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(long cpuRate) {
        this.cpuRate = cpuRate;
    }

    @Override
    public String toString() {
        return "Result{" +
                "_id=" + _id +
                ", method='" + method + '\'' +
                ", encryptionTime=" + encryptionTime +
                ", decryptionTime=" + decryptionTime +
                ", encryptedFileSize=" + encryptedFileSize +
                ", cpuRate=" + cpuRate +
                ", timestamp=" + timestamp +
                '}';
    }
}
