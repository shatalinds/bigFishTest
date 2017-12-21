package bigfish.bigfishtest.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import bigfish.bigfishtest.App;
import bigfish.bigfishtest.AuthService;
import bigfish.bigfishtest.R;
import bigfish.bigfishtest.model.ChangeStations;
import bigfish.bigfishtest.model.ChargeStationsModel;
import bigfish.bigfishtest.model.Type;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class ChargingStationAdapter extends RecyclerView.Adapter<ChargingStationAdapter.ViewHolder> {
    @Inject Context mCtx;
    @Inject LayoutInflater mLayoutInflater;
    @Inject @Named("authService") Retrofit mAuthRetrofit;

    private Activity mActivity;
    private ChargeStationsModel mChargeStationsModel;
    private AuthService mAuthService;

    public ChargingStationAdapter(Activity activity, ChargeStationsModel model) {
        ((App) activity.getApplication()).getAppComponent().inject(this);
        mAuthService = mAuthRetrofit.create(AuthService.class);

        this.mActivity = activity;
        this.mChargeStationsModel = model;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ChargingStationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.station_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChargingStationAdapter.ViewHolder holder, int position) {
        ChangeStations changeStation = mChargeStationsModel.getData().get(position);
        holder.rlItem.setTag(changeStation);

        String id = String.valueOf(changeStation.getId());
        if (!TextUtils.isEmpty(id))
            holder.tvId.setText("id: " + id);

        String nameRu = (String) changeStation.getNameRu();
        if (!TextUtils.isEmpty(nameRu)) {
            String nameEn = (String) changeStation.getNameEn();
            if (!TextUtils.isEmpty(nameEn)) {
                holder.tvName.setText("nameRu: " + nameRu + ", nameEn: " + nameEn);
            } else {
                holder.tvName.setText("nameRu: " + nameRu);
            }
        }

        String boxId = changeStation.getChargeBoxIdentity();
        if (!TextUtils.isEmpty(boxId))
            holder.tvBoxId.setText("chargeBoxIdentity: " + boxId);

        String billRate = String.valueOf(changeStation.getBillRate());
        if (!TextUtils.isEmpty(billRate))
            holder.tvBillRate.setText("billRate: " + billRate);

        String serial = (String) changeStation.getChargePointSerialNumber();
        if (!TextUtils.isEmpty(serial))
            holder.tvSerialNumber.setText("chargePointSerialNumber: " + serial);

        String coord = (String) changeStation.getCoordinates();
        if (!TextUtils.isEmpty(coord))
            holder.tvCoordinates.setText("coordinates: " + coord);

        String maxPower = changeStation.getMaxPower();
        if (!TextUtils.isEmpty(maxPower))
            holder.tvMaxPower.setText("maxPower: " + maxPower);

        String regStatus = changeStation.getRegistrationStatus().getName();
        if (!TextUtils.isEmpty(regStatus))
            holder.tvRegStatus.setText("registrationStatus: " + regStatus);

        StringBuilder connectors = new StringBuilder();
        connectors.append("connectors: ");
        for (int i = 0; i < changeStation.getConnectors().size(); i++) {
            List<Type> types = changeStation.getConnectors().get(i).getTypes();

            for (int j = 0; j < types.size(); j++)
                connectors.append(types.get(j).getName()).append(" ");

        }
        holder.tvConnectors.setText(connectors.toString());

        StringBuilder facilities = new StringBuilder();
        facilities.append("facilities: ");

        for (int i = 0; i < changeStation.getFacilities().size(); i++)
            facilities.append(changeStation.getFacilities().get(i).getNameRu()).append(" ");

        holder.tvFacilities.setText(facilities.toString());
    }

    @Override
    public int getItemCount() {
        return mChargeStationsModel.getData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlItem) RelativeLayout rlItem;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvBoxId) TextView tvBoxId;
        @BindView(R.id.tvBillRate) TextView tvBillRate;
        @BindView(R.id.tvSerialNumber) TextView tvSerialNumber;
        @BindView(R.id.tvConnectors) TextView tvConnectors;
        @BindView(R.id.tvCoordinates) TextView tvCoordinates;
        @BindView(R.id.tvFacilities) TextView tvFacilities;
        @BindView(R.id.tvId) TextView tvId;
        @BindView(R.id.tvMaxPower) TextView tvMaxPower;
        @BindView(R.id.tvRegStatus) TextView tvRegStatus;

        private void reqTime(Map<String, String> param) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
            alertDialog.setTitle(mCtx.getString(R.string.request_time));
            alertDialog.setMessage(mCtx.getString(R.string.request_charge));

            final EditText input = new EditText(mActivity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(10, 10, 10, 10);
            int maxLengthofEditText = 3;
            input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setIcon(R.drawable.ic_launcher_background);

            alertDialog.setPositiveButton(mCtx.getString(R.string.ok), (DialogInterface dialog, int which) -> {
                final int mHours = Integer.valueOf(input.getText().toString());

                if (mHours > 0 && mHours <= 480) {
                    param.put("expirationTime", String.valueOf(mHours));
                    mAuthService.charge_price_anticipate(param)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .filter(chargePriceAnticipateModel -> chargePriceAnticipateModel != null)
                            .subscribe(chargePriceAnticipateModel -> {
                                if (chargePriceAnticipateModel.getStatusCode() == 200) {
                                    String cost = String.valueOf(chargePriceAnticipateModel.getData().getCost());
                                    Toast.makeText(mCtx, mCtx.getString(R.string.price_complite) + " " + cost, Toast.LENGTH_LONG).show();
                                } else Toast.makeText(mCtx, (String) chargePriceAnticipateModel.getError(), Toast.LENGTH_LONG).show();
                            }, throwable -> Timber.e(throwable));
                } else {
                    if (mHours == 0)
                        Toast.makeText(mCtx, mCtx.getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    else Toast.makeText(mCtx, mCtx.getString(R.string.big_fail), Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.setNegativeButton(mCtx.getString(R.string.cancel), (DialogInterface dialog, int which) -> {
                dialog.cancel();
            });

            alertDialog.show();
        }

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            rlItem.setOnClickListener((View v1) -> {
                ChangeStations changeStations = (ChangeStations) rlItem.getTag();
                if (changeStations != null) {
                    Map<String, String> param = new HashMap<>();
                    param.put("stationId", String.valueOf(changeStations.getId()));
                    reqTime(param);
                }
            });
        }
    }
}