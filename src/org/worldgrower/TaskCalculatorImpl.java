/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MoveAction;
import org.worldgrower.terrain.Terrain;

public class TaskCalculatorImpl implements TaskCalculator, Serializable {

	private int maxDepth = 50;
	private static final NodeComparator NODE_COMPARATOR = new NodeComparator();
	private final OpenSet openSet;
	private final ClosedSet closedSet;
	
	public TaskCalculatorImpl(World world) {
		this.closedSet = new ClosedSet(world.getWidth(), world.getHeight());
		this.openSet = new OpenSet(world.getWidth(), world.getHeight());
	}
	
	public TaskCalculatorImpl(int worldWidth, int worldHeight) {
		this.closedSet = new ClosedSet(worldWidth, worldHeight);
		this.openSet = new OpenSet(worldWidth, worldHeight);
	}
	
	@Override
	public List<OperationInfo> calculateTask(WorldObject performer, World world, OperationInfo goal) {
		LocationWorldObjectsCache zone = (LocationWorldObjectsCache) world.getWorldObjectsCache(Constants.X, Constants.Y);
		Terrain terrain = world.getTerrain();
		
		WorldObject copyPerformer = performer.shallowCopy();
		
		closedSet.clear();
		openSet.clear();
		initializeOpenSet(performer, copyPerformer, goal, world);
		
		while(!openSet.isEmpty()) {
			Node current = openSet.poll();
			
			if (current.h == 0) {
				return constructTasks(performer, goal, current);
			}
			
			closedSet.add(current);
			
			if (current.g < maxDepth) {
				for(Node neighbourNode : neighbourNodes(current, copyPerformer, terrain, zone)) {
					if (!closedSet.contains(neighbourNode)) {
						if (!openSet.contains(neighbourNode)) {
							neighbourNode.h = distance(goal, copyPerformer, neighbourNode, world);
							 
							neighbourNode.parentNode = current;
							openSet.add(neighbourNode);
						}
					}
				}
			}
		}
		
		//throw new IllegalStateException("performer " + performer + " cannot calculate tasks to goal " + goal);
		return new ArrayList<>();
	}

	private List<OperationInfo> constructTasks(WorldObject performer, OperationInfo goal, Node current) {
		List<Node> reconstructedPath = reconstructPath(current);
		List<OperationInfo> result = new ArrayList<>(reconstructedPath.size());
		for(int i=reconstructedPath.size()-1; i>0; i--) {
			int xMovement = reconstructedPath.get(i-1).x - reconstructedPath.get(i).x;
			int yMovement = reconstructedPath.get(i-1).y - reconstructedPath.get(i).y;
			int[] args = new int[] { xMovement, yMovement };
			result.add(new OperationInfo(performer, performer, args, Actions.MOVE_ACTION));
		}
		result.add(goal);
		
		return result;
	}
	
	private void initializeOpenSet(WorldObject performer, WorldObject copyPerformer, OperationInfo goal, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		Node startNode = new Node(performerX, performerY, 0);
		startNode.h = distance(goal, copyPerformer, startNode, world);
		
		openSet.add(startNode);
	}
	
	private int distance(OperationInfo goal, WorldObject copyPerformer, Node node, World world) {
		copyPerformer.setPropertyUnchecked(Constants.X, node.x);
		copyPerformer.setPropertyUnchecked(Constants.Y, node.y);
		return goal.distance(copyPerformer, world);
	}
	
	private List<Node> reconstructPath(Node current) {
		List<Node> reconstructedPath = new ArrayList<>();
		reconstructedPath.add(current);
		while ((current = current.parentNode) != null) {
			reconstructedPath.add(current);
		}
		return reconstructedPath;
	}

	private List<Node> neighbourNodes(Node node, WorldObject performer, Terrain terrain, LocationWorldObjectsCache zone) {
		List<Node> result = new ArrayList<>(8);
		int newG = node.g + 1;
		addNodeToList(result, node.x - 1, node.y - 1, newG, performer, terrain, zone);
		addNodeToList(result, node.x - 1, node.y, newG, performer, terrain, zone);
		addNodeToList(result, node.x - 1, node.y + 1, newG, performer, terrain, zone);
		addNodeToList(result, node.x, node.y - 1, newG, performer, terrain, zone);
		addNodeToList(result, node.x, node.y + 1, newG, performer, terrain, zone);
		addNodeToList(result, node.x + 1, node.y - 1, newG, performer, terrain, zone);
		addNodeToList(result, node.x + 1, node.y, newG, performer, terrain, zone);
		addNodeToList(result, node.x + 1, node.y + 1, newG, performer, terrain, zone);
		return result;
	}
	
	private void addNodeToList(List<Node> list, int nodeX, int nodeY, int newG, WorldObject performer, Terrain terrain, LocationWorldObjectsCache zone) {
		if ((nodeX >= 0) && (nodeX < terrain.getWidth()) && 
				(nodeY >= 0) && (nodeY < terrain.getHeight()) && 
				zone.value(nodeX, nodeY) == 0 &&
				//TODO: call MoveAction::distance?
				MoveAction.performerCanMoveOnTerrain(performer, nodeX, nodeY, terrain)) {
			list.add(new Node(nodeX, nodeY, newG));
		}
	}

	private static final class Node {
		public final int x;
		public final int y;
		public final int g;
		public int h;
		public Node parentNode;
		
		public Node(int x, int y, int g) {
			this.x = x;
			this.y = y;
			this.g = g;
		}
		
		@Override
		public int hashCode() {
			int result = 7;
			result = 31 * result + x;
			result = 31 * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			Node other = (Node) obj;
			return (x == other.x) && (y == other.y);
		}

		@Override
		public String toString() {
			return "["+ x + ", " + y + "]";
		}	
	}
	
	// a ClosedSet works by flagging each tile with an int.
	// if the int is expected the tile is in the closed set.
	// to avoid clearing the flags each iteration, the expected int value is increased.
	// This way the performance of the contains and add method is improved over using a HashSet. 
	private static final class ClosedSet implements Serializable {
		private int[][] values;
		private int currentValue;
		
		public ClosedSet(int worldWidth, int worldHeight) {
			super();
			reset(worldWidth, worldHeight);
		}

		private void reset(int worldWidth, int worldHeight) {
			this.values = new int[worldWidth][worldHeight];
			this.currentValue = 1;
		}
		
		public void clear() {
			currentValue = (currentValue + 1) % Integer.MAX_VALUE;
			if (currentValue == 0) {
				reset(values.length, values[0].length);
			}
		}
		
		public void add(Node node) {
			values[node.x][node.y] = currentValue;
		}
		
		public boolean contains(Node node) {
			return values[node.x][node.y] == currentValue;
		}
	}
	
	// An OpenSet works by flagging each tile with an int, like the OpenSet.
	// It also keeps a sorted PriorityQueue to retrieve the closest Node.
	// The flag tiles are used because PriorityQueue::contains has O(n) time complexity,
	// while using the flag tiles has time complexity O(1).
	private static final class OpenSet implements Serializable {
		private final PriorityQueue<Node> queue;
		private int[][] values;
		private int currentValue;
		
		public OpenSet(int worldWidth, int worldHeight) {
			super();
			reset(worldWidth, worldHeight);
			
			queue = new PriorityQueue<>(NODE_COMPARATOR);
		}
		
		private void reset(int worldWidth, int worldHeight) {
			this.values = new int[worldWidth][worldHeight];
			this.currentValue = 1;
		}
		
		public void clear() {
			currentValue = (currentValue + 1) % Integer.MAX_VALUE;
			if (currentValue == 0) {
				reset(values.length, values[0].length);
			}
			
			queue.clear();
		}
		
		public void add(Node node) {
			values[node.x][node.y] = currentValue;
			
			queue.add(node);
		}
		
		public boolean contains(Node node) {
			return values[node.x][node.y] == currentValue;
		}
		
		public Node poll() {
			return queue.poll();
		}
		
		public boolean isEmpty() {
			return queue.isEmpty();
		}
	}
	
	private static final class NodeComparator implements Comparator<Node>, Serializable {

		@Override
		public int compare(Node node1, Node node2) {
			return ((node1.g + node1.h) - (node2.g + node2.h));
		}
	}
}
