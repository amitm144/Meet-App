package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.grab.GrabPollBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.*;
import superapp.logic.GrabsService;
import superapp.logic.MiniAppServices;
import superapp.util.exceptions.ForbbidenOperationException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.*;
import java.util.stream.Collectors;

import static superapp.data.ObjectTypes.*;

@Service("Grab")
public class GrabService implements GrabsService, MiniAppServices {
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	private final Random RANDOM = new Random();
	private final List<GrabCuisines> CUISINES = List.of(GrabCuisines.values());
	private final int CUISINES_SIZE = CUISINES.size();

	@Autowired
	public GrabService(SuperAppObjectEntityRepository objectRepository) {
		this.objectRepository = objectRepository;
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		String objectType = object.getType();
		if (!isValidObjectType(objectType) || ObjectTypes.valueOf(objectType) != GrabPoll)
			throw new InvalidInputException("Unknown object type");

		this.checkPollData(object);
	}

	@Override
	public Object runCommand(MiniAppCommandBoundary command) {

		SuperAppObjectIdWrapper targetObject = command.getTargetObject();
		UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
		String commandCase = command.getCommand();
		Map<String ,Object> commandAttribute = command.getCommandAttributes();

		SuperappObjectPK targetObjectKey = this.converter.idToEntity(targetObject.getObjectId());
		SuperAppObjectEntity poll = this.objectRepository.findById(targetObjectKey)
				.orElseThrow(() ->  new NotFoundException("Grab poll not found"));
		SuperAppObjectEntity group =
				poll.getParents()
				.stream()
				.findFirst() // grab poll can only be bound to one parent
				.orElseThrow(() ->  new NotFoundException("Grab poll is not bound to any group"));

		if (!isUserInGroup(group, invokedBy))
			throw new InvalidInputException("Invoking user is not part of this group");
		if (!(group.getActive() && poll.getActive()))
			throw new InvalidInputException("Cannot execute commands on an inactive group or poll");

		switch(commandCase) {
			case "addVote": {
				List<String> cuisinesStr = (List<String>)commandAttribute.get("cuisines");
				List<GrabCuisines> cuisines = new ArrayList<GrabCuisines>();
				cuisinesStr.stream().map(i -> cuisines.add(GrabCuisines.valueOf(i))).toList();
				Map<GrabCuisines, Integer> votes = this.addVote(poll, cuisines);

				Map<String,Object> rv = new HashMap<String,Object>();
				rv.put("votes",votes);
				poll.setObjectDetails(this.converter.detailsToString(rv));
				this.objectRepository.save(poll);


				return votes;
			}
			case "selectRandomly": { return this.selectRandomly(poll); }
			case "selectByMajority": { return this.selectByMajority(poll); }
			default: throw new NotFoundException("Unknown command");
		}
	}

	@Override
	public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {
		if (!parent.getType().equals(ObjectTypes.Group.name()))
			throw new InvalidInputException("Cannot bind poll to non-group objects");
		if(child.getParents().size() > 0)
			throw new ForbbidenOperationException("Grab poll can only be bound to one group");
	}

	@Override
	public Map<GrabCuisines, Integer>  addVote(SuperAppObjectEntity poll, List<GrabCuisines> votes) {
		Map<GrabCuisines, Integer> existingVotes = getVotesFromPoll(poll) ;
		for (GrabCuisines vote: votes) {
			if (existingVotes.containsKey(vote))
				existingVotes.replace(vote , existingVotes.get(vote) + 1 );
			else
				existingVotes.put(vote , 1);
		}
		return existingVotes;
	}

	@Override
	public Object selectRandomly(SuperAppObjectEntity poll) {
		GrabCuisines chosenCuisine = CUISINES.get(RANDOM.nextInt(CUISINES_SIZE));
		GrabPollBoundary selection =  new GrabPollBoundary(chosenCuisine, "");
		addSelectionToPoll(poll, selection);
		return disableAndSave(poll);
	}

//	@Override
//	public SuperAppObjectBoundary selectByMajority(SuperAppObjectEntity poll) {
//		Map<String, Object> pollDetails = this.converter.detailsToMap(poll.getObjectDetails());
//		GrabCuisines selected = null;
//		int totalVotes = 0;
//		for (Map.Entry<String, Object> entry : pollDetails.entrySet()) {
//			GrabCuisines cuisines = GrabCuisines.valueOf(entry.getKey());
//			Integer votes = (Integer)entry.getValue();
//			if (votes > totalVotes)
//				selected = cuisines;
//		}
//		addSelectionToPoll(poll, new GrabPollBoundary(selected, ""));
//		return disableAndSave(poll);
//	}


	@Override
	public SuperAppObjectBoundary selectByMajority(SuperAppObjectEntity poll) {
		Map<GrabCuisines, Integer> totalVotes =getVotesFromPoll(poll) ;

		Comparator<GrabCuisines> valueComparator = (k1, k2) -> {
			Integer v1 = totalVotes.get(k1);
			Integer v2 = totalVotes.get(k2);
			return v2.compareTo(v1);
		};

		Map<GrabCuisines, Integer> totalVotesSorted = new TreeMap<>(valueComparator);
		totalVotesSorted.putAll(totalVotes);
		GrabCuisines selected = totalVotesSorted.entrySet().iterator().next().getKey();

		poll = addSelectionToPoll(poll, new GrabPollBoundary(selected, "", totalVotesSorted)); //todo add sorted map
		return disableAndSave(poll);
	}


	private SuperAppObjectEntity addSelectionToPoll(SuperAppObjectEntity poll, GrabPollBoundary selection) {
		Map<String,Object> pollSelection= new HashMap<String,Object>();
		pollSelection.put("selection",selection);
		poll.setObjectDetails(this.converter.detailsToString(pollSelection));
		return poll ;
	}

	private SuperAppObjectBoundary disableAndSave(SuperAppObjectEntity poll) {
		poll.setActive(false);
		this.objectRepository.save(poll);
		return this.converter.toBoundary(poll);
	}

	private void checkPollData(SuperAppObjectBoundary poll) {
		if (!poll.getActive())
			throw new ForbbidenOperationException("Cannot operate on inactive poll");
		GrabPollBoundary selection = (GrabPollBoundary)poll.getObjectDetails().get("selection");
		if (selection != null) {
			disableAndSave(this.converter.toEntity(poll));
			throw new ForbbidenOperationException("Cannot operate on a poll that have ended");
		}
	}

	private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
		linkedMap.put("superapp", userId.getSuperapp());
		linkedMap.put("email", userId.getEmail());

		return ((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.contains(linkedMap);
	}

	private Map<GrabCuisines, Integer> getVotesFromPoll(SuperAppObjectEntity poll) {
		Map<GrabCuisines, Integer> existingVotes = new HashMap<GrabCuisines, Integer>();
		Map<String, Integer> existingVotesStr = (Map<String, Integer>) this.converter.detailsToMap(poll.getObjectDetails()).get("votes");
		existingVotesStr.forEach((i, j) -> existingVotes.put(GrabCuisines.valueOf(i), j));

		return existingVotes;
	}


}
