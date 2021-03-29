package br.com.module.pokeapi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.module.pokeapi.R
import br.com.module.pokeapi.models.Result
import kotlinx.android.synthetic.main.item_pokemon_preview.view.*

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.ArticleViewHolder>() {

    private val differCallback = object  : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonAdapter.ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pokemon_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PokemonAdapter.ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {

            tvName.text = article.name
            tvUrl.text = article.url

            setOnClickListener{
                onItemClickListener?.let { it(article) }
            }
        }
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((Result) -> Unit)? = null


    fun setOnItemClickListener(listener: (Result) -> Unit){
        onItemClickListener = listener
    }

}