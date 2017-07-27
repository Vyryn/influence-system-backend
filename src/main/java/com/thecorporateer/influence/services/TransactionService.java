package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private InfluenceRepository influenceRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DivisionRepository divisionRepository;

	public boolean transfer(Corporateer sender, Corporateer receiver, String message, int amount, InfluenceType type) {
		if (!validate(sender, receiver, amount)) {
			return false;
		}
		Division senderMainDivision = sender.getMainDivision();

		Department influenceDepartment;
		Division influenceDivision;

		if (senderMainDivision.equals(receiver.getMainDivision())) {
			influenceDivision = senderMainDivision;
			influenceDepartment = influenceDivision.getDepartment();
		} else if (senderMainDivision.getDepartment().equals(receiver.getMainDivision().getDepartment())) {
			influenceDivision = divisionRepository.findOne(1L);
			influenceDepartment = senderMainDivision.getDepartment();
		} else {
			influenceDivision = divisionRepository.findOne(1L);
			influenceDepartment = departmentRepository.findOne(1L);
		}

		sender.setTributes(sender.getTributes() - amount);
		sender = corporateerRepository.save(sender);

		List<Influence> influence = new ArrayList<>();
		influence.add(influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(receiver,
				influenceDepartment, influenceDivision, type));
		influence.add(influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(sender, influenceDepartment,
				influenceDivision, type));
		influence.get(0).setAmount(amount);
		influence.get(1).setAmount(amount);
		influenceRepository.save(influence);

		Transaction trans = new Transaction();
		trans.setType(type);
		trans.setAmount(amount);
		trans.setMessage(message);
		trans.setSender(sender);
		trans.setReceiver(receiver);
		trans.setDivision(influenceDivision);
		trans.setDepartment(influenceDepartment);
		transactionRepository.save(trans);

		return true;
	}

	private boolean validate(Corporateer sender, Corporateer receiver, int amount) {
		if (sender.getTributes() < amount) {
			return false;
		}
		if (sender.getId() == receiver.getId()) {
			return false;
		}

		return true;
	}

}