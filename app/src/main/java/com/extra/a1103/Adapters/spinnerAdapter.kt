import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.extra.a1103.R
import kotlinx.android.synthetic.main.popup.view.*

class spinnerAdapter(var context: Context,var array: Array<String>): BaseAdapter() {

    override fun getCount(): Int {
        return array.count()
    }

    override fun getItem(p0: Int): Any {
        return array.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var rootview=LayoutInflater.from(context).inflate(R.layout.popup,p2,false)
        rootview.voTypeText.text=array.get(p0)
        return rootview
    }

}