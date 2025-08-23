#ifndef NDARRAY_H
#define NDARRAY_H

#include <array>
#include <cstring>
#include <stdexcept>

namespace detailts
{
	template< typename T, size_t V >
	struct nested_init_list
	{
		using type = std::initializer_list< typename nested_init_list< T, V - 1 >::type >;
	};

	template< typename T >
	struct nested_init_list< T, 1 >
	{
		using type = std::initializer_list< T >;
	};

	template< typename T >
	struct nested_init_list< T, 0 >
	{
		using type = T;
	};
}	 // namespace detailts

template< typename T, size_t R >
class NDArray;

template< typename Y >
class NDArrayIterator
{
  public:
	using value_type = Y;
	using pointer = Y *;
	using reference = Y &;
	using difference_type = std::ptrdiff_t;
	using size_type = size_t;
	using iterator_category = std::random_access_iterator_tag;

	NDArrayIterator() = delete;

	NDArrayIterator(const NDArrayIterator &other) : ptr(other.ptr) {}

	NDArrayIterator(pointer ptr) : ptr(ptr) {}

	NDArrayIterator &operator+=(const difference_type n)
	{
		ptr += n;
		return *this;
	}

	NDArrayIterator operator+(difference_type n) const noexcept { return NDArrayIterator(*this) += n; }

	friend NDArrayIterator operator+(difference_type n, const NDArrayIterator &a) noexcept { return a + n; }

	NDArrayIterator &operator-=(const difference_type n) noexcept { return *this += -n; }

	NDArrayIterator operator-(difference_type n) const noexcept { return NDArrayIterator(*this) += -n; }

	difference_type operator-(const NDArrayIterator &b) const noexcept { return ptr - b.ptr; }

	friend NDArrayIterator operator-(const NDArrayIterator &a, difference_type n) noexcept { return a - n; }

	reference operator[](difference_type n) const noexcept { return *(ptr + n); }

	reference operator*() const noexcept { return *ptr; }

	bool operator<(const NDArrayIterator &b) const noexcept { return b - *this > 0; }

	bool operator>(const NDArrayIterator &b) const noexcept { return b < *this; }

	bool operator<=(const NDArrayIterator &b) const noexcept { return !(*this > b); }

	bool operator>=(const NDArrayIterator &b) const noexcept { return !(*this < b); }

	bool operator==(const NDArrayIterator &b) const noexcept { return ptr == b.ptr; }

	bool operator!=(const NDArrayIterator &b) const noexcept { return !(*this == b); }

	pointer operator->() const noexcept { return ptr; }

	NDArrayIterator &operator++() noexcept { return *this += 1; }

	NDArrayIterator operator++(int) noexcept
	{
		NDArrayIterator tmp = *this;
		++*this;
		return tmp;
	}

	NDArrayIterator &operator--() noexcept { return *this -= 1; }

	operator NDArrayIterator< const Y >() const noexcept { return NDArrayIterator< const Y >(ptr); }

  private:
	pointer ptr = nullptr;
};

namespace detailts
{

	template< typename T, size_t R, typename U, size_t D >
	class ViewAbstract
	{
	  public:
		using reference = U &;
		using difference_type = std::ptrdiff_t;
		using size_type = size_t;
		using value_type = U;
		using iterator = NDArrayIterator< U >;

		ViewAbstract() noexcept = default;

		ViewAbstract(const ViewAbstract &other) noexcept :
			begin_(other.begin_), end_(other.end_), begin_cont_(other.begin_cont_), dims_begin_(other.dims_begin_)
		{
		}

		ViewAbstract &operator=(const ViewAbstract &other) noexcept
		{
			if (this != &other)
				ViewAbstract(other).swap(*this);
			return *this;
		}

		void swap(ViewAbstract &other) noexcept
		{
			std::swap(begin_, other.begin_);
			std::swap(end_, other.end_);
			std::swap(begin_cont_, other.begin_cont_);
			std::swap(dims_begin_, other.dims_begin_);
		}

		ViewAbstract &operator=(const T &value)
		{
			*begin_ = value;
			return *this;
		}

		ViewAbstract(ViewAbstract &&other) noexcept :
			begin_(other.begin_), end_(other.end_), begin_cont_(other.begin_cont_), dims_begin_(other.dims_begin_)
		{
			other.begin_ = nullptr;
			other.end_ = nullptr;
			other.begin_cont_ = nullptr;
			other.dims_begin_ = nullptr;
		}

		ViewAbstract &operator=(ViewAbstract &&other)
		{
			if (this != &other)
			{
				begin_ = other.begin_;
				end_ = other.end_;
				begin_cont_ = other.begin_cont_;
				dims_begin_ = other.dims_begin_;
				other.begin_ = nullptr;
				other.end_ = nullptr;
				other.begin_cont_ = nullptr;
				other.dims_begin_ = nullptr;
			}
			return *this;
		}
		~ViewAbstract() = default;

		ViewAbstract(NDArray< T, R > &arr) noexcept :
			begin_(arr.get_start_data()), end_(arr.get_end_data()), begin_cont_(arr.get_start_data()), dims_begin_(arr.dim_data_)
		{
		}

		ViewAbstract(const NDArray< T, R > &arr) noexcept :
			begin_(arr.get_start_data()), end_(arr.get_end_data()), begin_cont_(arr.get_start_data()), dims_begin_(arr.dim_data_)
		{
		}

		ViewAbstract(const iterator &begin, const iterator &end) noexcept : begin_(begin), end_(end), begin_cont_(begin)
		{
		}

		ViewAbstract(const iterator &begin, const iterator &end, size_type *dims_begin_, const iterator &begin_cont_) noexcept
			: begin_(begin), end_(end), begin_cont_(begin_cont_), dims_begin_(dims_begin_)
		{
		}

		ViewAbstract &operator+=(difference_type n) noexcept
		{
			const size_type step = end_ - begin_;
			begin_ += step * n;
			end_ += step * n;
			return *this;
		}

		ViewAbstract &operator++() { return *this += 1; }

		ViewAbstract operator++(int)
		{
			ViewAbstract tmp = *this;
			++*this;
			return tmp;
		}

		ViewAbstract operator+(difference_type n) const { return ViewAbstract(*this) += n; }

		friend ViewAbstract operator+(difference_type n, const ViewAbstract &a) { return a + n; }

		ViewAbstract &operator-=(difference_type n) { return *this += -n; }

		ViewAbstract &operator--() { return *this -= 1; }

		ViewAbstract operator-(difference_type n) const { return ViewAbstract(*this) += -n; }

		difference_type operator-(const ViewAbstract &b) const { return begin_ - b.begin_; }

		reference operator*() const noexcept
			requires(D == 0)
		{
			return *begin_;
		}

		ViewAbstract operator*() const noexcept(D > 0) { return *this; }
		ViewAbstract< T, R, U, D - 1 > operator[](int n)
			requires(D > 0)
		{
			const size_type step = (end_ - begin_) / *dims_begin_;
			const size_type start = step * n;
			const size_type end = start + step;
			return ViewAbstract< T, R, U, D - 1 >(begin_ + start, begin_ + end, dims_begin_ + 1, begin_cont_);
		}

		bool operator==(const ViewAbstract &b) const noexcept
			requires std::equality_comparable< T >
		{
			return begin_cont_ == b.begin_cont_ && std::equal(begin_, b.begin_, end_, b.end_);
		}

		bool operator==(const T &value) const noexcept
			requires(D == 0 && std::equality_comparable< T >)
		{
			return *begin_ == value;
		}

		bool operator!=(const ViewAbstract &b) const noexcept { return !(*this == b); }

		bool operator<(const ViewAbstract &b) const noexcept
		{
			return begin_cont_ == b.begin_cont_ && b.begin_ - begin_ > 0;
		}

		bool operator>(const ViewAbstract &b) const noexcept
		{
			return begin_cont_ == b.begin_cont_ && b.begin_ < begin_;
		}

		bool operator<=(const ViewAbstract &b) const noexcept { return !(begin_ > b.begin_); }

		bool operator>=(const ViewAbstract &b) const noexcept { return !(begin_ < b.begin_); }

		size_type count() const noexcept { return !dims_begin_ ? 0 : *dims_begin_; }

		size_type size() const noexcept { return count(); }

		size_type total_count() const noexcept { return end_ - begin_; }

		size_type dim() const noexcept { return D; }

		bool is_equal(const NDArray< T, R > &other) const noexcept { return check_structure(other); }

		bool is_equal(const NDArray< T, R > &other) const noexcept
			requires std::equality_comparable< T >
		{
			return check_structure(other) && std::equal(begin_, end_, other.data_);
		}

		template< typename SizeList >
		ViewAbstract< T, R, U, R > reshape(const SizeList &dims)
		{
			if (dims.size() > 0)
			{
				dims_begin_ = new size_type[dims.size()];
				for (size_type i = 0; i < dims.size(); ++i)
				{
					dims_begin_[i] = dims[i];
				}
			}
			return *this;
		}

	  protected:
		bool check_structure(const NDArray< T, R > &other) const noexcept
		{
			if (D != other.dim_)
				return false;
			if (total_count() != other.total_)
				return false;
			if (dims_begin_[0] != other.dim_data_[0])
				return false;
			for (size_type k = 0; k < D; ++k)
			{
				if (dims_begin_[k] != other.dim_data_[k])
					return false;
			}
			return true;
		}

		NDArrayIterator< U > begin_;
		NDArrayIterator< U > end_;

		NDArrayIterator< U > begin_cont_;
		size_type *dims_begin_;
	};

	template< typename T, size_t R, typename U, size_t D >
	class View;

	template< typename T, size_t R, typename U >
	class View< T, R, U, 1 > : public ViewAbstract< T, R, U, 1 >
	{
	  public:
		using iterator = NDArrayIterator< U >;
		using const_iterator = NDArrayIterator< const U >;
		using ViewAbstract< T, R, U, 1 >::ViewAbstract;

		U &operator[](int n) { return this->begin_[n]; }

		U const &operator[](int n) const noexcept { return this->begin_[n]; }

		iterator begin() noexcept { return this->begin_; }

		const_iterator begin() const noexcept { return this->begin_; }

		const_iterator cbegin() const noexcept { return this->begin_; }

		iterator end() noexcept { return this->end_; }

		const_iterator end() const noexcept { return this->end_; }

		const_iterator cend() const noexcept { return this->end_; }
	};

	template< typename T, size_t R, typename U, size_t D >
		requires(D > 1)
	class View< T, R, U, D > : public ViewAbstract< T, R, U, D >
	{
	  public:
		using ViewAbstract< T, R, U, D >::ViewAbstract;

		View< T, R, T, D - 1 > begin() noexcept { return this->operator[](0); }

		View< T, R, const T, D - 1 > begin() const noexcept { return this->operator[](0); }

		View< T, R, const T, D - 1 > cbegin() const noexcept { return begin(); }

		View< T, R, T, D - 1 > end() noexcept { return this->operator[](*this->dims_begin_); }

		View< T, R, const T, D - 1 > end() const noexcept { return this->operator[](*this->dims_begin_); }

		View< T, R, const T, D - 1 > cend() const noexcept { return end(); }
	};

	template< typename T, size_t R, typename U >
	class View< T, R, U, 0 > : public ViewAbstract< T, R, U, 0 >
	{
	  public:
		using ViewAbstract< T, R, U, 0 >::ViewAbstract;
	};

}	 // namespace detailts

template< typename T, size_t R >
class NDArray
{
  public:
	template< typename H, size_t E, typename L, size_t P >
	friend class detailts::ViewAbstract;

	using value_type = T;
	using pointer = T *;
	using const_pointer = const T *;
	using reference = T &;
	using const_reference = const T &;
	using difference_type = std::ptrdiff_t;
	using size_type = size_t;
	using iterator = NDArrayIterator< T >;
	using const_iterator = NDArrayIterator< const T >;

	class NDArrayView : public detailts::View< T, R, T, R >
	{
	  public:
		using detailts::View< T, R, T, R >::View;
	};

	class NDArrayConstView : public detailts::View< T, R, const T, R >
	{
	  public:
		using detailts::View< T, R, const T, R >::View;
	};

	using view = NDArrayView;
	using const_view = NDArrayConstView;

	// rule of five

	NDArray()
	{
		// dim_data_ = new size_type[R > 0 ? R : 1];
		// constexpr dim_data_ = size_type[R > 0 ? R : 1];
		std::fill(dim_data_, dim_data_ + (R > 0 ? R : 1), 0);
		total_ = 0;
		data_ = nullptr;
	}

	~NDArray() noexcept
	{
		for (size_t i = 0; i < total_count(); ++i)
			data_[i].~T();
		operator delete(data_, std::align_val_t(alignof(T)));
		delete[] dim_data_;
	}

	NDArray(const NDArray &other) : dim_(other.dim_), total_(other.total_)
	{
		data_ = static_cast< T * >(operator new(sizeof(T) * total_, std::align_val_t(alignof(T))));
		size_type i = 0;
		try
		{
			for (; i < total_; ++i)
				new (data_ + i) T(other.data_[i]);
		} catch (...)
		{
			for (size_type j = 0; j < i; ++j)
				data_[j].~T();
			operator delete(data_, std::align_val_t(alignof(T)));
			data_ = nullptr;
			throw;
		}
		if constexpr (R > 0)
			dim_data_ = new size_type[R];
		else
			dim_data_ = new size_type[1]{ 1 };
		std::copy(other.dim_data_, other.dim_data_ + R, dim_data_);
	}

	NDArray &operator=(const NDArray &other)
	{
		if (this != &other)
			NDArray(other).swap(*this);
		return *this;
	};

	NDArray(NDArray &&other) noexcept :
		dim_(other.dim_), total_(other.total_), data_(other.data_), dim_data_(other.dim_data_)
	{
		other.data_ = nullptr;
		other.dim_data_ = nullptr;
		other.total_ = 0;
		other.dim_ = 0;
	}

	NDArray &operator=(NDArray &&other) noexcept
	{
		if (this != &other)
		{
			operator delete(data_, std::align_val_t(alignof(T)));
			delete[] dim_data_;

			(*this).swap(other);

			other.data_ = nullptr;
			other.dim_data_ = nullptr;
			other.total_ = 0;
			other.dim_ = 0;
		}
		return *this;
	}

	// init list

	NDArray(typename detailts::nested_init_list< T, R >::type init) :
		total_(1), dim_data_(R > 0 ? new size_type[R]() : new size_type[1]{ 1 })
	{
		fill_sizes< R >(init, R);
		for (size_type k = 0; k < R; ++k)
			total_ *= dim_data_[k];
		data_ = static_cast< T * >(operator new(sizeof(T) * total_, std::align_val_t(alignof(T))));
		for (size_type i = 0; i < total_; ++i)
			new (data_ + i) T();
		fill_data< R >(init, 0, total_, R);
	}

	// sizes of every dims

	template< typename... Args >
	NDArray(Args... dims) : total_(1), dim_data_(R > 0 ? new size_type[R] : new size_type[1]{ 1 })
	{
		std::array< size_type, R > dims_array{ (dims)... };

		for (size_type i = 0; i < R; ++i)
		{
			dim_data_[i] = dims_array[i];
			total_ *= dim_data_[i];
		}

		data_ = static_cast< T * >(operator new(sizeof(T) * total_, std::align_val_t(alignof(T))));
		for (size_type i = 0; i < total_; ++i)
			new (data_ + i) T();
	}

	// default value

	template< typename... Args >
	NDArray(T &default_value, Args... i) : NDArray(i...)
	{
		for (size_type k = 0; k < total_; ++k)
		{
			data_[k] = default_value;
		}
	}

	// view

	NDArray(const view &view) :
		total_(view.end_ - view.begin_), dim_data_(R > 0 ? new size_type[R]() : new size_type[1]{ 1 })
	{
		data_ = static_cast< T * >(operator new(sizeof(T) * (view.end_ - view.begin_), std::align_val_t(alignof(T))));
		for (size_type k = 0; k < R; ++k)
			dim_data_[k] = view.dim_data_[k];
		for (size_type k = 0; k < total_; ++k)
			data_[k] = view.begin_[k];
	}

	NDArray(const const_view &view) :
		total_(view.end_ - view.begin_), dim_data_(R > 0 ? new size_type[R]() : new size_type[1]{ 1 })
	{
		data_ = static_cast< T * >(operator new(sizeof(T) * (view.end_ - view.begin_), std::align_val_t(alignof(T))));
		for (size_type k = 0; k < R; ++k)
			dim_data_[k] = view.dim_data_[k];
		for (size_type k = 0; k < total_; ++k)
			data_[k] = view.begin_[k];
	}

	// iterators

	NDArray(const_iterator &begin, const_iterator &end) : dim_(1), total_(end - begin), dim_data_(new size_type[1])
	{
		data_ = static_cast< T * >(operator new(sizeof(T) * total_, std::align_val_t(alignof(T))));
		dim_data_[0] = 1;
		size_type k = 0;
		for (auto it = begin; it != end; ++it)
		{
			data_[k++] = *it;
		}
	}

	// methods

	size_type count() const noexcept { return !dim_data_ ? 0 : dim_data_[0]; }

	size_type size() const noexcept { return count(); }

	size_type total_count() const noexcept { return get_end_data() - get_start_data(); }

	size_type dim() const noexcept { return dim_; }

	void swap(NDArray &other) noexcept
	{
		std::swap(total_, other.total_);
		std::swap(data_, other.data_);
		std::swap(dim_data_, other.dim_data_);
		std::swap(dim_, other.dim_);
	}

	friend void swap(NDArray &a, NDArray &b) noexcept { a.swap(b); }

	auto begin() noexcept { return view(get_start_data(), get_end_data(), dim_data_, get_start_data()).begin(); }

	auto begin() const noexcept
	{
		return const_view(get_start_data(), get_end_data(), dim_data_, get_start_data()).begin();
	}

	auto cbegin() const noexcept { return begin(); }

	auto end() noexcept { return view(get_start_data(), get_end_data(), dim_data_, get_start_data()).end(); }

	auto end() const noexcept
	{
		return const_view(get_start_data(), get_end_data(), dim_data_, get_start_data()).end();
	}

	auto cend() const noexcept { return end(); }

	auto at(size_type index) const
	{
		if (index >= dim_data_[0])
			throw std::out_of_range("NDArray::at");
		detailts::View< T, R, const T, R - 1 > view = (*this)[index];
		return view;
	}

	template< typename SizeList >
		requires std::integral< typename SizeList::value_type >
	view reshape(const SizeList &list) const
	{
		size_type size = 1;
		for (auto &x : list)
		{
			size *= x;
			if (x <= 0)
				throw std::out_of_range("NDArray::reshape");
		}
		if (size != total_count())
			throw std::out_of_range("NDArray::reshape");
		size_type *dims = nullptr;
		if constexpr (R > 0)
		{
			dims = new size_type[R];
			size_type k = 0;
			for (auto &d : list)
				dims[k++] = d;
		}
		else
			dims = new size_type[1]{ 1 };
		return view(get_start_data(), get_end_data(), dims, get_start_data());
	}

	bool empty() const noexcept { return get_start_data() == get_end_data(); }

	pointer data() noexcept { return data_; }

	const_pointer data() const noexcept { return data_; }

	bool is_equal(const NDArray &other) const noexcept
	{
		return const_view(get_start_data(), get_end_data(), dim_data_, get_start_data()).is_equal(other);
	}

	bool is_equal(const NDArray &other) const noexcept
		requires std::equality_comparable< T >
	{
		return const_view(get_start_data(), get_end_data(), dim_data_, get_start_data()).is_equal(other);
	}

	size_type max_size() const noexcept
	{
		return std::allocator_traits< std::allocator< T > >::max_size(std::allocator< T >());
	}

	// operators

	detailts::View< T, R, T, R - 1 > operator[](const size_type i) noexcept
		requires(R > 1)
	{
		const size_type step = total_ / *dim_data_;
		const size_type start = step * i;
		const size_type en = start + step;

		return detailts::View< T, R, T, R - 1 >(get_start_data() + start, get_start_data() + en, dim_data_ + 1, get_start_data());
	}

	detailts::View< T, R, const T, R - 1 > operator[](const size_type i) const noexcept
		requires(R > 1)
	{
		const size_type step = total_ / *dim_data_;
		const size_type start = step * i;
		const size_type en = start + step;

		return detailts::View< T, R, const T, R - 1 >(get_start_data() + start, get_start_data() + en, dim_data_ + 1, get_start_data());
	}

	T const &operator[](const size_type i) const noexcept
		requires(R == 1)
	{
		return data_[i];
	}

	T &operator[](const size_type i) noexcept
		requires(R == 1)
	{
		return data_[i];
	}

	bool operator==(const NDArray &other) const noexcept
		requires std::equality_comparable< T >
	{
		return total_ == other.total_ && std::equal(get_start_data(), get_end_data(), other.get_start_data());
	}

	bool operator!=(const NDArray &other) const { return !(this == other); }

  private:
	iterator get_start_data() noexcept { return iterator(data_); }

	const_iterator get_start_data() const noexcept { return const_iterator(data_); }

	iterator get_end_data() noexcept { return iterator(data_ + total_); }

	const_iterator get_end_data() const noexcept { return const_iterator(data_ + total_); }

	template< size_type Z >
	void fill_sizes(const typename detailts::nested_init_list< T, Z >::type &init, const size_type current_level)
	{
		if constexpr (Z > 0)
		{
			dim_data_[R - current_level] = std::max(dim_data_[R - current_level], init.size());
		}

		if constexpr (Z > 1)
		{
			for (auto it = init.begin(); it != init.end(); ++it)
			{
				fill_sizes< Z - 1 >(*it, current_level - 1);
			}
		}
	}

	template< size_type Z >
	void fill_data(const typename detailts::nested_init_list< T, Z >::type &init, const size_type current_offset, size_type step, const size_type depth)
	{
		size_type idx = 0;
		step /= (dim_data_[R - depth] == 0 ? 1 : dim_data_[R - depth]);

		if constexpr (Z > 1)
		{
			for (auto &it : init)
			{
				fill_data< Z - 1 >(it, current_offset + step * idx++, step, depth - 1);
			}
		}
		if constexpr (Z == 1)
		{
			for (auto it : init)
			{
				data_[current_offset + idx++] = it;
			}
		}
		if constexpr (Z == 0)
		{
			data_[current_offset] = init;
		}
	}

	size_type dim_ = R;
	size_type total_ = 0;
	T *data_;
	size_type *dim_data_;
};

#endif	  // NDARRAY_H
