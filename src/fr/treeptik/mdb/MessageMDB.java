package fr.treeptik.mdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.treeptik.mdb.xml.ListCommandes;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test") })
public class MessageMDB implements MessageListener {

	public void onMessage(Message message) {

		TextMessage textMessage = (TextMessage) message;

		// Pour l'envoie de fichier xml
		JAXBContext jaxbContext = null;

		try {
			jaxbContext = JAXBContext.newInstance("fr.treeptik.mdb.xml");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		File fileEcriture = new File("commande.xml");

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(fileEcriture,
					false));
			try {
				bufferedWriter.write(textMessage.getText());
				bufferedWriter.close();

			} catch (IOException | JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Lecture du fichier client
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ListCommandes commandes = (ListCommandes) unmarshaller
					.unmarshal(new File("commande.xml"));
			System.out.println(commandes.getCommande().size());

		} catch (Exception e) {
			System.out.println("commande");
			e.printStackTrace();
		}

	}

}
