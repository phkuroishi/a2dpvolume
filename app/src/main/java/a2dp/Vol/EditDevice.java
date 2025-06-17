package a2dp.Vol;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;

import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationManagerCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EditDevice extends Activity {

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        Save();
        closedb();
        EditDevice.this.finish();
        super.onBackPressed();
    }

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        Save();
        closedb();
        EditDevice.this.finish();
        super.onCreateNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onNavigateUp() {
        Save();
        closedb();
        EditDevice.this.finish();
        return super.onNavigateUp();
    }

    private Button sb;
    private EditText fdesc2;
    private CheckBox fgloc;
    private CheckBox fsetvol;
    private SeekBar fvol;
    private EditText fapp;
    private EditText fbt;
    private CheckBox fwifi;
    private CheckBox fapprestart;
    private CheckBox fappkill;
    private CheckBox fenableGPS;
    private CheckBox fenableTTS;
    private CheckBox fsetpv;
    private SeekBar fphonev;
    private SeekBar fsmsdelaybar;
    private TextView fsmsdelaybox, tv2, ttsdelay, mediadelay, tvstream, tvmediavol, tvincallVol;
    private SeekBar fvoldelaybar;
    private TextView fvoldelaybox;
    private CheckBox frampVol;
    private CheckBox fautoVol;
    private CheckBox fsilent;
    private CheckBox fsleepBox;
    private CheckBox fcarmodeBox;
    private RadioButton iconradio0, iconradio1, iconradio2, iconradio3, iconradio4, streamradio0, streamradio1, streamradio2;
    private RadioGroup streamgroup, icongroup;
    private LinearLayout l1, l2;

    SharedPreferences preferences;
    private boolean TTsEnabled;

    public String btd;
    private btDevice device;
    private MyApplication application;
    private DeviceDB myDB; // database of device data stored in SQlite
    private String pname;
    private String appaction;
    private String appdata;
    private String apptype;
    private boolean apprestart;
    private boolean appkill;
    private boolean enabletts;
    private static final int DIALOG_PICK_APP_TYPE = 3;
    private static final int DIALOG_WARN_STOP_APP = 5;
    private static final int DIALOG_BITLY = 6;
    private static final String[] APP_TYPE_OPTIONS = {"Choose App",
            "Create Shortcut",
            "Custom Intent", "Clear App Selection"};
    private static final int ACTION_CHOOSE_APP = 2;
    private static final int ACTION_CUSTOM_INTENT = 6;
    private static final int ACTION_CHOOSE_FROM_PROVIDER = 11;
    private static final int ACTION_CREATE_HOME_SCREEN_SHORTCUT = 14;
    private static final int FETCH_HOME_SCREEN_SHORTCUT = 15;
    private static final int ACTION_CHOOSE_APP_CUSTOM = 16;
    private static final int ACTION_ADD_PACKAGE = 17;
    private static final int MUSIC_STREAM = 0;
    private static final int IN_CALL_STREAM = 1;
    private static final int ALARM_STREAM = 2;
    private static final String EXTRA_BTD = "btd";

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edit_item);

        // Show the Up button in the action bar.
        setupActionBar();

        this.application = (MyApplication) this.getApplication();
        this.myDB = new DeviceDB(application);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.sb = this.findViewById(R.id.EditDevSavebutton);
        Button cb = this.findViewById(R.id.EditDevCancelbutton);
        Button startapp = this.findViewById(R.id.chooseAppButton);
        Button connbt = this.findViewById(R.id.chooseBTbutton);
        this.fdesc2 = this.findViewById(R.id.editDesc2);
        this.fgloc = this.findViewById(R.id.checkCaptLoc);
        this.fsetvol = this.findViewById(R.id.checkSetVol);
        this.fvol = this.findViewById(R.id.seekBarVol);
        this.fapp = this.findViewById(R.id.editApp);
        this.fapprestart = this
                .findViewById(R.id.appRestartCheckbox);
        this.fappkill = this.findViewById(R.id.appKillCheckbox);
        this.fbt = this.findViewById(R.id.editBtConnect);
        this.fwifi = this.findViewById(R.id.checkwifi);

        this.fenableTTS = this.findViewById(R.id.enableTTSBox);
        this.fsetpv = this.findViewById(R.id.checkSetpv);
        this.fsilent = this.findViewById(R.id.silentBox);
        this.fphonev = this.findViewById(R.id.seekPhoneVol);
        this.fsmsdelaybar = this.findViewById(R.id.SMSdelayseekBar);
        this.fsmsdelaybox = this.findViewById(R.id.SMSdelaytextView);
        this.fvoldelaybar = this.findViewById(R.id.VolDelaySeekBar);
        this.fvoldelaybox = this.findViewById(R.id.VolDelayTextView);
        this.tv2 = this.findViewById(R.id.textView2);
        this.frampVol = this.findViewById(R.id.rampBox);
        this.fautoVol = this.findViewById(R.id.autoVolcheckBox);
        this.icongroup = this.findViewById(R.id.radioGroupIcon);
        this.iconradio0 = this.findViewById(R.id.iconradio0);
        this.iconradio1 = this.findViewById(R.id.iconradio1);
        this.iconradio2 = this.findViewById(R.id.iconradio2);
        this.iconradio3 = this.findViewById(R.id.iconradio3);
        this.iconradio4 = this.findViewById(R.id.iconradio4);
        this.streamgroup = this.findViewById(R.id.radioGroupStream);
        this.streamradio0 = this.findViewById(R.id.streamradio0);
        this.streamradio1 = this.findViewById(R.id.streamradio1);
        this.streamradio2 = this.findViewById(R.id.streamradio2);
        this.l1 = this.findViewById(R.id.LinearLayout1);
        this.l2 = this.findViewById(R.id.LinearLayout2);
        this.ttsdelay = this.findViewById(R.id.textViewTTSDelay);
        this.mediadelay = this.findViewById(R.id.textViewMediaDelay);
        this.tvstream = this.findViewById(R.id.textViewStream);
        this.tvmediavol = this.findViewById(R.id.textViewMediaVolume);
        this.tvincallVol = this.findViewById(R.id.textViewInCallVol);
        this.fsleepBox = this.findViewById(R.id.checkBoxSleep);
        this.fcarmodeBox = this.findViewById(R.id.checkBoxLaunchCar);

        preferences = PreferenceManager
                .getDefaultSharedPreferences(application);
        TTsEnabled = preferences.getBoolean("enableTTS", false);

        btd = getIntent().getStringExtra("btd"); // get the mac address of the
        // device to edit

        if (btd == null) {
            if (savedInstanceState != null) {
                btd = savedInstanceState.getString(EXTRA_BTD);
            }
        }

        // this should only even be possible if the task was in the background
        // and got killed because of low memory - so just finish the activity (without showing it)
        if (btd == null) {
            finish();
            return;
        }

        device = myDB.getBTD(btd);

        fdesc2.setText(device.desc2);
        fgloc.setChecked(device.isGetLoc());
        fsetvol.setChecked(device.isSetV());

      /*  if (android.os.Build.VERSION.SDK_INT > 27) {
            fsetvol.setChecked(false);
            setVolVisibility();
        }*/

        fvol.setMax(Objects.requireNonNull(am).getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        fvol.setProgress(device.defVol);
        fapp.setText(device.getPname());
        fbt.setText(device.getBdevice());
        fwifi.setChecked(device.isWifi());


        if (device == null)
            connbt.setEnabled(false);
        pname = device.getPname();
        appaction = device.getAppaction();
        appdata = device.getAppdata();
        apptype = device.getApptype();
        apprestart = device.isApprestart();
        appkill = device.isAppkill();

        fapprestart.setChecked(apprestart);
        fappkill.setChecked(appkill);

        fenableTTS.setChecked(device.isEnableTTS());
        fsetpv.setChecked(device.isSetpv());
        fphonev.setMax(am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
        fphonev.setProgress(device.getPhonev());
        fsmsdelaybar.setMax(20);
        fsmsdelaybar.setOnSeekBarChangeListener(smsdelaySeekBarProgress);
        fsmsdelaybox.setText(device.smsdelay + "s");
        fsmsdelaybar.setProgress(device.getSmsdelay());
        fvoldelaybar.setMax(20);
        fvoldelaybar.setOnSeekBarChangeListener(voldelaySeekBarProgress);
        fvoldelaybox.setText(device.voldelay + "s");
        fvoldelaybar.setProgress(device.getVoldelay());
        frampVol.setChecked(device.isVolramp());
        fautoVol.setChecked(device.isAutovol());
        fsilent.setChecked(device.isSilent());
        fsleepBox.setChecked(device.isSleep());
        fcarmodeBox.setChecked(device.isCarmode());

        switch (device.getIcon()) {
            case R.drawable.car2:
                iconradio0.setChecked(true);
                break;
            case R.drawable.headset:
                iconradio1.setChecked(true);
                break;
            case R.drawable.jack:
                iconradio2.setChecked(true);
                break;
            case R.drawable.usb:
                iconradio3.setChecked(true);
                break;
            case R.drawable.icon5:
                iconradio4.setChecked(true);
                break;
            default:
                device.setIcon(R.drawable.car2);
                iconradio0.setChecked(true);
                break;
        }

        switch (device.getSmsstream()) {
            case MUSIC_STREAM:
                streamradio0.setChecked(true);
                break;
            case IN_CALL_STREAM:
                streamradio1.setChecked(true);
                break;
            case ALARM_STREAM:
                streamradio2.setChecked(true);
                break;
            default:
                streamradio0.setChecked(true);
        }


        setTTSVisibility();
        fenableTTS.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                setTTSVisibility();
            }

        });

        setMediaVisibility();
        fsetvol.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                setMediaVisibility();

            }

        });

        setInCallVisibility();
        fsetpv.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                setInCallVisibility();
            }

        });

        setAppVisibility();

        tv2.requestFocus(); // prevent jumping around screen
        vUpdateApp();

        sb.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                Save();
                closedb();
                EditDevice.this.finish();
            }
        });

        cb.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                closedb();
                EditDevice.this.finish();
            }

        });
        // Show list of packages. This one loads fast but is too cryptic for
        // normal users
        startapp.setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View arg0) {
                final PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(0);
                final String[] lstring = new String[packages.size()];
                int i = 0;
                for (int n = 0; n < packages.size(); n++) {
                    Intent itent = pm.getLaunchIntentForPackage(packages.get(n).packageName);
                    if (packages.get(n).icon > 0 && packages.get(n).enabled
                            && itent != null) {
                        lstring[i] = packages.get(n).packageName;
                        // lstring[i] = itent.toUri(0);
                        i++;
                    } else {
                        // This does not have an icon or is not enabled
                    }
                }

                // get just the ones with an icon. This assumes packages without
                // icons are likely not ones a user needs.
                final String[] ls2 = new String[i];
                for (int j = 0; j < i; j++) {
                    ls2[j] = lstring[j];
                }
                java.util.Arrays.sort(ls2); // sort the array

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        a2dp.Vol.EditDevice.this);
                builder.setTitle("Pick a package");
                builder.setItems(ls2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        fapp.setText(ls2[item]);
                    }
                });
                AlertDialog alert = builder.create();

                alert.show();

                return false;
            }
        });

        // The more friendly app chooser. However, this loads slow.
        startapp.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                AlertDialog.Builder adb2 = new AlertDialog.Builder(
                        a2dp.Vol.EditDevice.this);
                adb2.setTitle(R.string.ea_ti_app);
                adb2.setItems(APP_TYPE_OPTIONS, mAppTypeDialogOnClick);
                adb2.create();
                adb2.show();

                // Intent in = new Intent(getBaseContext(), AppChooser.class);
                // startActivityForResult(in, 0);
            }
        });

        connbt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!myDB.getDb().isOpen()
                        && !myDB.getDb().isDbLockedByCurrentThread())
                    myDB = new DeviceDB(application);
                final Vector<btDevice> vec = myDB.selectAlldb();
                int j = vec.size();
                for (int i = 0; i < j; i++) {
                    if ((vec.get(i).mac.length() < 17)) {
                        vec.remove(i);
                        j--;
                        i--;
                    }
                }

                vec.trimToSize();
                final String[] lstring = new String[vec.size() + 1];
                for (int i = 0; i < vec.size(); i++) {
                    lstring[i] = vec.get(i).desc2;
                }
                lstring[vec.size()] = "none";

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        a2dp.Vol.EditDevice.this);
                builder.setTitle("Bluetooth Device");
                builder.setItems(lstring,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item < vec.size())
                                    fbt.setText(vec.get(item).mac);
                                else
                                    fbt.setText("");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setVolVisibility() {

        // This was created for Android 9 since this feature should be obselete but people want it anyway
/*        if (android.os.Build.VERSION.SDK_INT > 27) {
            fsetvol.setChecked(false);
            fsetvol.setVisibility(CheckBox.GONE);
        } else {
            fsetvol.setVisibility(CheckBox.VISIBLE);
        }*/

        fsetvol.setVisibility(CheckBox.VISIBLE);
        setMediaVisibility();
    }

    private void setMediaVisibility() {
        if (fsetvol.isChecked()) {
            tvmediavol.setVisibility(TextView.VISIBLE);
            fvol.setVisibility(SeekBar.VISIBLE);
            fautoVol.setVisibility(CheckBox.VISIBLE);
            frampVol.setVisibility(CheckBox.VISIBLE);
            l2.setVisibility(LinearLayout.VISIBLE);
            mediadelay.setVisibility(TextView.VISIBLE);
        } else {
            tvmediavol.setVisibility(TextView.GONE);
            fvol.setVisibility(SeekBar.GONE);
            fautoVol.setVisibility(CheckBox.GONE);
            frampVol.setVisibility(CheckBox.GONE);
            l2.setVisibility(LinearLayout.GONE);
            mediadelay.setVisibility(TextView.GONE);
        }
    }

    private void setTTSVisibility() {
        if (fenableTTS.isChecked()) {
            l1.setVisibility(LinearLayout.VISIBLE);
            ttsdelay.setVisibility(TextView.VISIBLE);
            tvstream.setVisibility(TextView.VISIBLE);
            streamgroup.setVisibility(RadioGroup.VISIBLE);
        } else {
            l1.setVisibility(LinearLayout.GONE);
            ttsdelay.setVisibility(TextView.GONE);
            tvstream.setVisibility(TextView.GONE);
            streamgroup.setVisibility(RadioGroup.GONE);
        }
    }

    private void setInCallVisibility() {
        if (fsetpv.isChecked()) {
            tvincallVol.setVisibility(TextView.VISIBLE);
            fphonev.setVisibility(SeekBar.VISIBLE);
        } else {
            tvincallVol.setVisibility(TextView.GONE);
            fphonev.setVisibility(SeekBar.GONE);
        }
    }

    private void setAppVisibility() {
        if (fapp.getText().length() > 0) {
            fapp.setVisibility(EditText.VISIBLE);
            fapprestart.setVisibility(CheckBox.VISIBLE);
            fappkill.setVisibility(CheckBox.VISIBLE);
            fsleepBox.setVisibility(CheckBox.GONE);
        } else {
            fapp.setVisibility(EditText.GONE);
            fapprestart.setVisibility(CheckBox.GONE);
            fappkill.setVisibility(CheckBox.GONE);
            fsleepBox.setVisibility(CheckBox.GONE);
        }
    }


    OnSeekBarChangeListener smsdelaySeekBarProgress = new OnSeekBarChangeListener() {

        public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            fsmsdelaybox.setText(progress + "s");
            //fsmsdelaybar.requestFocus();

        }

        public void onStartTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }

        public void onStopTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }
    };

    OnSeekBarChangeListener voldelaySeekBarProgress = new OnSeekBarChangeListener() {

        public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            fvoldelaybox.setText(progress + "s");
            //fvoldelaybar.requestFocus();

        }

        public void onStartTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }

        public void onStopTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }
    };

    private void Save() {
        if (fdesc2.length() < 1)
            device.setDesc2(device.desc1);
        else
            device.setDesc2(fdesc2.getText().toString());

        device.setSetV(fsetvol.isChecked());
        device.setDefVol(fvol.getProgress());
        device.setGetLoc(fgloc.isChecked());
        device.setPname(pname);
        device.setBdevice(fbt.getText().toString());
        device.setWifi(fwifi.isChecked());
        //device.setEnablegps(fenableGPS.isChecked());
        device.setAppaction(appaction);
        device.setAppdata(appdata);
        device.setApptype(apptype);
        apprestart = fapprestart.isChecked();
        device.setApprestart(apprestart);
        appkill = fappkill.isChecked();
        device.setAppkill(appkill);
        enabletts = fenableTTS.isChecked();
        device.setEnableTTS(enabletts);
        device.setSetpv(fsetpv.isChecked());
        device.setPhonev(fphonev.getProgress());
        device.setSmsdelay(fsmsdelaybar.getProgress());
        device.setVoldelay(fvoldelaybar.getProgress());
        device.setVolramp(frampVol.isChecked());
        device.setAutovol(fautoVol.isChecked());
        device.setSilent(fsilent.isChecked());
        device.setSleep(fsleepBox.isChecked());
        device.setCarmode(fcarmodeBox.isChecked());

        switch (icongroup.getCheckedRadioButtonId()) {
            case R.id.iconradio0:
                device.setIcon(R.drawable.car2);
                break;
            case R.id.iconradio1:
                device.setIcon(R.drawable.headset);
                break;
            case R.id.iconradio2:
                device.setIcon(R.drawable.jack);
                break;
            case R.id.iconradio3:
                device.setIcon(R.drawable.usb);
                break;
            case R.id.iconradio4:
                device.setIcon(R.drawable.icon5);
                break;

            default:
                device.setIcon(R.drawable.car2);
        }

        switch (streamgroup.getCheckedRadioButtonId()) {
            case R.id.streamradio0:
                device.setSmsstream(MUSIC_STREAM);
                break;
            case R.id.streamradio1:
                device.setSmsstream(IN_CALL_STREAM);
                break;
            case R.id.streamradio2:
                device.setSmsstream(ALARM_STREAM);
                break;
        }

        // if the user wants TTS but it was not enabled
        if (!TTsEnabled && fenableTTS.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("enableTTS", true);
            editor.apply();

            // If the user enabled reading notifications, check settings and launch setting dialog if its not enabled
            Set<String> list = NotificationManagerCompat.getEnabledListenerPackages(getBaseContext());
            Boolean listenerEnabled = false;
            for (String item : list) {
                if (item.equalsIgnoreCase("a2dp.Vol")) listenerEnabled = true;
            }

            if (!listenerEnabled) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        }

        sb.setText("Saving");
        try {
            myDB.update(device);
            // Reload the device list in the main page
            final String Ireload = "a2dp.Vol.main.RELOAD_LIST";
            Intent itent = new Intent();
            itent.setAction(Ireload);
            itent.putExtra("device", "");
            application.sendBroadcast(itent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void closedb() {
        myDB.getDb().close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_CHOOSE_APP:
                    pname = data.getStringExtra(AppChooser.EXTRA_PACKAGE_NAME);
                    appaction = "";
                    apptype = "";
                    appdata = "";

                    vUpdateApp();
                    break;
                case ACTION_ADD_PACKAGE:
                    pname = data.getStringExtra(AppChooser.EXTRA_PACKAGE_NAME);
                    vUpdateApp();
                    break;

                case ACTION_CHOOSE_APP_CUSTOM:
                    pname = data.getStringExtra(AppChooser.EXTRA_PACKAGE_NAME);
                    vUpdateApp();
                    break;

                case ACTION_CUSTOM_INTENT:

                    pname = "";
                    if (data != null) {
                        pname = "Intent";
                        if (data.getAction() != null) {
                            appaction = data.getAction();
                        } else {
                            appaction = "";
                        }
                        if (data.getData() != null) {
                            appdata = data.getData().toString();
                        } else {
                            appdata = "";
                        }
                        if (data.getType() != null) {
                            apptype = data.getType();
                        } else {
                            apptype = "";
                        }
                    } else {
                        break;
                    }


                    if (pname.equals("")) {
                        pname = "Intent";
                    }
                    vUpdateApp();
                    break;

                case ACTION_CREATE_HOME_SCREEN_SHORTCUT:
                    startActivityForResult(data, FETCH_HOME_SCREEN_SHORTCUT);
                    break;

            }

        } else {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private DialogInterface.OnClickListener mAppTypeDialogOnClick = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            Intent i;
            switch (which) {
                case 0:
                    // Select App
                    i = new Intent(getBaseContext(), AppChooser.class);
                    startActivityForResult(i, ACTION_CHOOSE_APP);
                    break;
                case 1:
                    // Create Shortcut
                    i = new Intent(Intent.ACTION_PICK_ACTIVITY);
                    i.putExtra(Intent.EXTRA_INTENT, new Intent(
                            Intent.ACTION_CREATE_SHORTCUT));
                    i.putExtra(Intent.EXTRA_TITLE, "Create a Shortcut");
                    startActivityForResult(i, ACTION_CREATE_HOME_SCREEN_SHORTCUT);
                    break;

                case 2:
                    // Custom Intent
                    i = new Intent(getBaseContext(), CustomIntentMaker.class);
                    i.putExtra("alarm_custom_action", appaction);
                    i.putExtra("alarm_custom_data", appdata);
                    i.putExtra("alarm_custom_type", apptype);
                    i.putExtra("alarm_package_name", pname);
                    startActivityForResult(i, ACTION_CUSTOM_INTENT);
                    break;

                case 3:
                    // Clear App
                    pname = "";
                    appaction = "";
                    appdata = "";
                    apptype = "";

                    vUpdateApp();
                    break;
            }
        }

    };

    private void vUpdateApp() {
        device.setAppaction(appaction);
        device.setAppdata(appdata);
        device.setApptype(apptype);
        device.setPname(pname);

        if (device.hasIntent()) {
            if (pname != null && pname.length() > 3)
                fapp.setText(pname);
            else if (appdata != null) {
                fapp.setText(appdata);
            } else if (appaction != null) {
                fapp.setText(appaction);
            } else {
                fapp.setText("Custom");
            }
        } else
            fapp.setText("");
        setAppVisibility();
    }

    public static String getIntentUri(Intent i) {
        String rtr = "";
        try {
            Method m = Intent.class.getMethod("toUri",
                    int.class);
            rtr = (String) m.invoke(i,
                    Intent.class.getField("URI_INTENT_SCHEME").getInt(null));
        } catch (Exception e) {
            // rtr = i.toURI();
        }
        return rtr;
    }


    // save the current btd value (mac) in case this activity gets killed while in background
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BTD, btd);
    }
}
