package com.example.view;

public class MyChartItem {
		    private String x;
		    private float y;
		    private float l;
		    public MyChartItem(String vx, float vy,float l) {
		        this.x = vx;
		        this.y = vy;
		        this.l = l;
		    }
		 
		    public String getX() {
		        return x;
		    }
		 
		    public void setX(String x) {
		        this.x = x;
		    }
		 
		    public float getY() {
		        return y;
		    }
		 
		    public void setY(float y) {
		        this.y = y;
		    }

			public float getL() {
				return l;
			}

			public void setL(float l) {
				this.l = l;
			}
}
