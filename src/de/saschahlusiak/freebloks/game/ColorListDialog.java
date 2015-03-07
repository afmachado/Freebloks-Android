package de.saschahlusiak.freebloks.game;

import de.saschahlusiak.freebloks.Global;
import de.saschahlusiak.freebloksvip.R;
import de.saschahlusiak.freebloks.controller.GameMode;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ColorListDialog extends Dialog implements OnItemClickListener {
	DialogInterface.OnClickListener listener;
	ListView list;
	ColorListAdapter adapter;
	
	public ColorListDialog(Context context, final DialogInterface.OnClickListener listener) {
		super(context);

		this.listener = listener;
		
		setContentView(LayoutInflater.from(context).inflate(R.layout.color_list_dialog, null));
		list = (ListView)findViewById(android.R.id.list);
		adapter = new ColorListAdapter(context);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(this);
		
		findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				listener.onClick(ColorListDialog.this, -1);
			}
		});
	}
	
	void setGameMode(GameMode gamemode) {
		setTitle(getContext().getResources().getStringArray(R.array.game_modes)[gamemode.ordinal()]);
		adapter.setGameMode(gamemode);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		listener.onClick(this, (int)id);
	}
	

	static class ColorListAdapter extends ArrayAdapter<String> {
		String[] colors;
		GameMode gamemode;
		
		public ColorListAdapter(Context context) {
			super(context, R.layout.color_list_item, android.R.id.text1);

			colors = context.getResources().getStringArray(R.array.color_names);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			View c = v.findViewById(R.id.color);
			
			
			LayerDrawable ld = (LayerDrawable)getContext().getResources().getDrawable(R.drawable.bg_card_1);
			GradientDrawable item;
			
			item = ((GradientDrawable)ld.findDrawableByLayerId(R.id.shadow));
			item.setColor(Global.PLAYER_BACKGROUND_COLOR[Global.getPlayerColor((int)getItemId(position), gamemode)]);

			item = ((GradientDrawable)ld.findDrawableByLayerId(R.id.color1));
			item.setColor(Global.PLAYER_FOREGROUND_COLOR[Global.getPlayerColor((int)getItemId(position), gamemode)]);
//			c.setBackgroundColor(Global.PLAYER_FOREGROUND_COLOR[Global.getPlayerColor((int)getItemId(position), gamemode)]);
			
			c.setBackgroundDrawable(ld);

			return v;
		}
		
		public void setGameMode(GameMode gamemode) {
			clear();
			
			switch (gamemode)
			{
			case GAMEMODE_2_COLORS_2_PLAYERS:
				add(colors[1]); // blue
				add(colors[3]); // red
				break;
			case GAMEMODE_DUO:
			case GAMEMODE_JUNIOR:
				add(colors[5]);
				add(colors[6]);
				break;
				
			case GAMEMODE_4_COLORS_2_PLAYERS:
			case GAMEMODE_4_COLORS_4_PLAYERS:
				add(colors[1]); // blue
				add(colors[2]); // Yellow
				add(colors[3]); // blue
				add(colors[4]); // green
				break;
				
			default:
				throw new RuntimeException("Whopsie, gamemode " + gamemode.ordinal() + " not implemented");
			}
			
			this.gamemode = gamemode;
			notifyDataSetChanged();
		}
		
		@Override
		public long getItemId(int position) {
			switch (gamemode) {
			case GAMEMODE_2_COLORS_2_PLAYERS:
			case GAMEMODE_DUO:
			case GAMEMODE_JUNIOR:
				if (position == 0)
					return 0;
				else
					return 2;
				
			default:
				return position;
			}
		}
	}

}