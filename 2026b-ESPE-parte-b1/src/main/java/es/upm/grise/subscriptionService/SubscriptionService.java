package es.upm.grise.subscriptionService;

import java.util.ArrayList;
import java.util.Collection;

import es.upm.grise.subscriptionService.exceptions.ExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NonExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NullUserException;
import es.upm.grise.subscriptionService.exceptions.UserDoesNotHaveEmailException;

public class SubscriptionService {

	private Collection <User> subscribers;
	private EmailService emailService;
	
	/* 
	 * Constructor
	 */
	
	public SubscriptionService(EmailService emailService) {
		
		this.emailService = emailService;
		subscribers = new ArrayList<User>();
		
	}

	/* 
	 * Method to test
	 */
	
	public void addSubscriber(User user) throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		
		if(user == null) {
			
			throw new NullUserException();	
			
		}
		
		if(subscribers.contains(user)) {

			throw new ExistingUserException();
						
		}
		
		if(user.getDeliveryType() != Delivery.LOCAL) {
		
			if(user.getEmail() == null) {
				
				throw new UserDoesNotHaveEmailException();			
				
			}
		}

		subscribers.add(user);
	
	}
	
	/* 
	 * Method to test
	 */
	
	public void removeSubscriber(User user) throws NullUserException, NonExistingUserException {
		
		if(user == null) {
			
			throw new NullUserException();
			
		}
			
		if(!subscribers.contains(user)) {
			
			throw new NonExistingUserException();
			
		}
		
		subscribers.remove(user);
		
	}
	
	/* 
	 * Method to test
	 */
	
	public int sendMessage(Message message) {
		
		int numDiscardedMessages = 0;
		
		for(User user : subscribers) {
			
			if(user.getDeliveryType() == Delivery.LOCAL) {
				
				user.saveMessage(message);
				
			}
			
			if(user.getDeliveryType() == Delivery.DO_NOT_DELIVER) {
				
				user.saveMessage(new Message(message.getId(), "Ha perdido Ud. un mensaje"));	
				numDiscardedMessages = numDiscardedMessages + 1;
				
			}
			
			emailService.sendMessage(user, message);

		} 
		
		return numDiscardedMessages;
		
	}

}
