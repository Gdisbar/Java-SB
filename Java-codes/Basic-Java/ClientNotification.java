
interface Notification{
	String sendNotification(String message);
}

class EmailNotification implements Notification{
	protected String formatMessage(String message) {
        return message.split(":")[0] + " - Base : " + message.split(":")[1];
    }

	public String sendNotification(String message){
		return formatMessage("Email Notification : " + message);
	}
}

class SMSNotification implements Notification{
	public String sendNotification(String message){
		return "SMS Notification : "+message;
	}
}

class PushNotification implements Notification{
	public String sendNotification(String message){
		return "Push Notification : "+message;
	}
}

class NotificationFactory {
	// private String message;
	// protected NotificationFactory(String message){
	// 	this.message = message;
	// }
    protected Notification createNotification(String type)throws IllegalArgumentException{
    	if (type.equalsIgnoreCase("email")) {
            return new EmailNotification();
        } else if (type.equalsIgnoreCase("sms")) {
            return new SMSNotification();
        } else if (type.equalsIgnoreCase("push")) {
            return new PushNotification();
        } else {
            throw new IllegalArgumentException("Invalid notification type: " + type);
        }
    	// try{
    	// 	if(type.toLowerCase().contains("mail")){
	    // 		EmailNotification emailNotification = new EmailNotification();
	    // 		return emailNotification.sendNotification(message);
	    // 	}else if(type.toLowerCase().contains("sms")){
	    // 		SMSNotification smsNotification = new SMSNotification();
	    // 		return smsNotification.sendNotification(message);
	    // 	}else if(type.toLowerCase().contains("push")){
	    // 		PushNotification pushNotification = new PushNotification();
	    // 		return pushNotification.sendNotification(message);
	    // 	}
    	// }catch(Exception e){
    	// 	System.err.println("Exception in "+e.getMessage());
    	// }
    	// return "Unknown type notification ";
    }
}


class ClientNotification{
	public static void main(String[] args) {
		// try{
		// 	System.out.println(notification.createNotification("Email"));
		// 	System.out.println(notification.createNotification("SMS"));
		// 	System.out.println(notification.createNotification("push"));
		// 	System.out.println(notification.createNotification("unknown"));
		// }catch(Exception e){
		// 	System.err.println("Exception in "+e.getMessage());
		// }
		NotificationFactory notificationFactory = new NotificationFactory();
        String message = "testing notification";
		try {
            Notification emailNotification = notificationFactory.createNotification("email");
            System.out.println(emailNotification.sendNotification(message));

            Notification smsNotification = notificationFactory.createNotification("SMS");
            System.out.println(smsNotification.sendNotification(message));

            Notification pushNotification = notificationFactory.createNotification("push");
            System.out.println(pushNotification.sendNotification(message));

            Notification unknownNotification = notificationFactory.createNotification("unknown");
            System.out.println(unknownNotification.sendNotification(message));

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
		
	}
}