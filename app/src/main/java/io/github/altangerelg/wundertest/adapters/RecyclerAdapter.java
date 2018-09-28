package io.github.altangerelg.wundertest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.altangerelg.wundertest.R;
import io.github.altangerelg.wundertest.models.Car;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public Context context;
    public List<Car> cars;

    public RecyclerAdapter(Context context, List<Car> cars) {
        this.context = context;
        this.cars = cars;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView car_address, car_engine_type, car_exterior, car_fuel, car_interior, car_name,
                car_vin;

        public ViewHolder(View itemView) {
            super(itemView);

            this.car_address = (TextView) itemView.findViewById(R.id.car_address);
            this.car_engine_type = (TextView) itemView.findViewById(R.id.car_engine_type);
            this.car_exterior = (TextView) itemView.findViewById(R.id.car_exterior);
            this.car_fuel = (TextView) itemView.findViewById(R.id.car_fuel);
            this.car_interior = (TextView) itemView.findViewById(R.id.car_interior);
            this.car_name = (TextView) itemView.findViewById(R.id.car_name);
            this.car_vin = (TextView) itemView.findViewById(R.id.car_vin);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent,
                false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.car_address.setText(cars.get(position).getAddress());
        holder.car_engine_type.setText(cars.get(position).getEngineType());
        holder.car_exterior.setText(cars.get(position).getExterior());
        holder.car_fuel.setText(String.valueOf(cars.get(position).getFuel()));
        holder.car_interior.setText(cars.get(position).getInterior());
        holder.car_name.setText(cars.get(position).getName());
        holder.car_vin.setText(cars.get(position).getVin());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}