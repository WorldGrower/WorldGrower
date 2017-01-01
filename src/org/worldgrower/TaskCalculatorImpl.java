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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.worldgrower.actions.Actions;

public class TaskCalculatorImpl implements TaskCalculator, Serializable {

	private int maxDepth = 63;
	
	@Override
	public List<OperationInfo> calculateTask(WorldObject performer, World world, OperationInfo goal) {
		LocationWorldObjectsCache zone = (LocationWorldObjectsCache) world.getWorldObjectsCache(Constants.X, Constants.Y);
		
		WorldObject copyPerformer = performer.shallowCopy();
		
		Set<Node> closedSet = new HashSet<>();
		TreeSet<Node> openSet = createOpenSet(performer);
		
		while(!openSet.isEmpty()) {
			Node current = openSet.first();
			
			if (distance(goal, copyPerformer, current, world) == 0) {
				List<OperationInfo> result = new ArrayList<>();
				List<Node> reconstructedPath = reconstructPath(current); 
				for(int i=reconstructedPath.size()-1; i>0; i--) {
					int xMovement = reconstructedPath.get(i-1).x - reconstructedPath.get(i).x;
					int yMovement = reconstructedPath.get(i-1).y - reconstructedPath.get(i).y;
					int[] args = new int[] { xMovement, yMovement };
					result.add(new OperationInfo(performer, performer, args, Actions.MOVE_ACTION));
				}
				result.add(goal);
				
				return result;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			if (current.g < maxDepth) {
				for(Node neighbourNode : neighbourNodes(current, world, zone)) {
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
	
	private TreeSet<Node> createOpenSet(WorldObject performer) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		Node startNode = new Node(performerX, performerY, 0);
		
		TreeSet<Node> openSet = new TreeSet<>(new NodeComparator());
		openSet.add(startNode);
		
		return openSet;
	}
	
	private int distance(OperationInfo goal, WorldObject copyPerformer, Node node, World world) {
		copyPerformer.setPropertyUnchecked(Constants.X, node.x);
		copyPerformer.setPropertyUnchecked(Constants.Y, node.y);
		return goal.distance(copyPerformer, world);
	}
	
	private List<Node> reconstructPath(Node current) {
		List<Node> reconstructedPath = new ArrayList<>();
		reconstructedPath.add(current);
		while (current != null) {
			current = current.parentNode;
			if (current != null) {
				reconstructedPath.add(current);
			}
		}
		return reconstructedPath;
	}

	private List<Node> neighbourNodes(Node node, World world, LocationWorldObjectsCache zone) {
		List<Node> result = new ArrayList<>();
		int newG = node.g + 1;
		addNodeToList(result, new Node(node.x - 1, node.y - 1, newG), world, zone);
		addNodeToList(result, new Node(node.x - 1, node.y, newG), world, zone);
		addNodeToList(result, new Node(node.x - 1, node.y + 1, newG), world, zone);
		addNodeToList(result, new Node(node.x, node.y - 1, newG), world, zone);
		addNodeToList(result, new Node(node.x, node.y + 1, newG), world, zone);
		addNodeToList(result, new Node(node.x + 1, node.y - 1, newG), world, zone);
		addNodeToList(result, new Node(node.x + 1, node.y, newG), world, zone);
		addNodeToList(result, new Node(node.x + 1, node.y + 1, newG), world, zone);
		return result;
	}
	
	private void addNodeToList(List<Node> list, Node neighbourNode, World world, LocationWorldObjectsCache zone) {
		if ((neighbourNode.x >= 0) && (neighbourNode.x < world.getWidth()) && 
				(neighbourNode.y >= 0) && (neighbourNode.y < world.getHeight()) && 
				zone.value(neighbourNode.x, neighbourNode.y) == 0) {
			list.add(neighbourNode);
		}
	}

	private static class Node {
		public int x;
		public int y;
		public int g;
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
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "["+ x + ", " + y + "]";
		}	
	}
	
	private static class NodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node node1, Node node2) {
			return ((node1.g + node1.h) - (node2.g + node2.h));
		}
	}
}
