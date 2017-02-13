package com.undergrowth.interview;

import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 对象克隆
 * @date 2017-02-10-9:11
 */
public class ObjectClone {

    class ObjectCloneInter implements  Cloneable{

        private int age ;
        private String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return "ObjectCloneInter{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Test
    public void objectCloneTest() throws CloneNotSupportedException {
        ObjectCloneInter  objectCloneInter=new ObjectCloneInter();
        objectCloneInter.setAge(28);
        objectCloneInter.setName("under");
        System.out.println(objectCloneInter);
        ObjectCloneInter cloneObjectCloneInter= (ObjectCloneInter) objectCloneInter.clone();
        System.out.println(cloneObjectCloneInter);
    }

    class Annoyance extends Exception {}
    class Sneeze extends Annoyance {}
    class Human {
        public  void excep()
                throws Exception {
            try {
                try {
                    throw new Sneeze();
                }
                catch ( Annoyance a ) {
                    System.out.println("Caught Annoyance");
                    throw a;
                }
            }
            catch ( Sneeze s ) {
                System.out.println("Caught Sneeze");
                return ;
            }
            finally {
                System.out.println("Hello World!");
            }
        }
    }

    @Test
    public void excepTest() throws Exception {
        Human human=new Human();
        human.excep();
    }

}
