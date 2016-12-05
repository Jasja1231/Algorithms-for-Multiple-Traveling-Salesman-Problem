/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.Arrays;

public class Permutations <T> {
 private T[] arr;
 public T[]currentPerm;
 private int[] permSwappings;

 public Permutations(T[] arr) {
  this(arr,arr.length);
 }

 public Permutations(T[] arr, int permSize) {
  this.arr = arr.clone();
  this.permSwappings = new int[permSize];
  for(int i = 0;i < permSwappings.length;i++)
   permSwappings[i] = i;
 }

 public T[] next() {
  if (arr == null)
   return null;

  T[] res = Arrays.copyOf(arr, permSwappings.length);
  //Prepare next
  int i = permSwappings.length-1;
  while (i >= 0 && permSwappings[i] == arr.length - 1) {
   swap(i, permSwappings[i]); //Undo the swap represented by permSwappings[i]
   permSwappings[i] = i;
   i--;
  }

  if (i < 0)
   arr = null;
  else {   
   int prev = permSwappings[i];
   swap(i, prev);
   int next = prev + 1;
   permSwappings[i] = next;
   swap(i, next);
  }
  currentPerm = res;
  return res;
 }

 private void swap(int i, int j) {
  T tmp = arr[i];
  arr[i] = arr[j];
  arr[j] = tmp;
 }

}