package superapp.data.split;

import superapp.converters.SuperAppObjectConverter;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.GroupEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SplitTransaction {

    private HashMap<UserEntity,Double> groupDebts;
    private UserEntity userPaid;
    private Date timestamp;
    private String description;
    private double originalPayment;
    private boolean isOpen;
    private String id;//TODO DB gives an ID

    private SuperAppObjectConverter converter ;

    public SplitTransaction(SuperAppObjectEntity group, UserEntity user, Date timestamp, String description, double originalPayment) { //balance must be greater than 0
        this.converter = new SuperAppObjectConverter();

        this.userPaid = user;
        this.timestamp = timestamp;
        this.description = description;
        this.originalPayment = originalPayment;
        this.isOpen= true;

        initGroupDebts(group);
        this.groupDebts.put(userPaid,originalPayment);
    }

    private void initGroupDebts(SuperAppObjectEntity group) {
        this.groupDebts = new HashMap<UserEntity,Double>();
        List<UserEntity> allUsers = (List<UserEntity>) converter.toBoundary(group).getObjectDetails().get("allUsers");
        for (UserEntity user:allUsers)
            groupDebts.put(user,0.0);
    }

    public UserEntity getUserPaid() {
        return userPaid;
    }
    public void setUserPaid(UserEntity user) {
        this.userPaid = userPaid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getOriginalPayment() {
        return originalPayment;
    }

    public void setOriginalPayment(double originalPayment) {
        this.originalPayment = originalPayment;
    }

    public HashMap<UserEntity, Double> getGroupDebts() {
        return groupDebts;
    }

    public void setGroupDebts(HashMap<UserEntity, Double> groupDebts) {
        this.groupDebts = groupDebts;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


        public void ComputeBank(UserEntity user) {
            double userdebt = this.groupDebts.get(user);
            double payedUserDebts = this.groupDebts.get(userPaid);
            this.groupDebts.put(this.userPaid,payedUserDebts+userdebt);
            this.groupDebts.put(user,0.0);
            this.setOpen(this.groupDebts.get(userPaid) ==0?false:true); // if userPayed debt equals to 0--> trasnacion close
        }
    @Override
    public String toString() {
        return "$"+ originalPayment +"/"+this.groupDebts.keySet().size()+" per member || "+ getDescription() + "|| paid by "+ getUserPaid() + "|| At " + getTimestamp();
    }

}

