CREATE TABLE IF NOT EXISTS public.cart (
    user_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16594)
-- Name: order_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.order_status (
    id integer NOT NULL,
    name character varying NOT NULL,
    description character varying(60)
);


ALTER TABLE public.order_status OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 16593)
-- Name: order_status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.order_status ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.order_status_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 16563)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.orders (
    id bigint NOT NULL,
    date date NOT NULL,
    consumer bigint NOT NULL,
    status integer NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16562)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 229 (class 1259 OID 16573)
-- Name: orders_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.orders_products (
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL,
    price integer NOT NULL
);


ALTER TABLE public.orders_products OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16442)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.users (
    id bigint NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(240) NOT NULL,
    password character varying(255) NOT NULL,
    registration_date date
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 4857 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE users; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.users IS 'Таблица хранящая информацию о пользователях';


--
-- TOC entry 226 (class 1259 OID 16561)
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16468)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.product (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    price integer NOT NULL,
    discount integer DEFAULT 0,
    rating real DEFAULT 0,
    reviews_count integer DEFAULT 0,
    amount integer DEFAULT 1 NOT NULL,
    description character varying(255),
    seller_id bigint NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16467)
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 223 (class 1259 OID 16523)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.role (
    id integer NOT NULL,
    name character varying(32) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16545)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.role ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 16480)
-- Name: seller; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.seller (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.seller OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16479)
-- Name: seller_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.seller ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.seller_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 16530)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.users_roles (
    user_id bigint NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- TOC entry 4681 (class 2606 OID 16522)
-- Name: seller name_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seller
    ADD CONSTRAINT name_unique_seller UNIQUE (name);


--
-- TOC entry 4695 (class 2606 OID 16600)
-- Name: order_status order_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_status
    ADD CONSTRAINT order_status_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.order_status
    ADD CONSTRAINT name_unique_os UNIQUE (name);

--
-- TOC entry 4691 (class 2606 OID 16567)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 4693 (class 2606 OID 16577)
-- Name: orders_products orders_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_pkey PRIMARY KEY (order_id, product_id);


--
-- TOC entry 4689 (class 2606 OID 16534)
-- Name: users_roles pk_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT pk_id PRIMARY KEY (user_id, role_id);


--
-- TOC entry 4675 (class 2606 OID 16446)
-- Name: users pk_person; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT pk_person PRIMARY KEY (id);


--
-- TOC entry 4679 (class 2606 OID 16478)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 4685 (class 2606 OID 16529)
-- Name: role role_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_name_key UNIQUE (name);


--
-- TOC entry 4687 (class 2606 OID 16527)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 4683 (class 2606 OID 16484)
-- Name: seller seller_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seller
    ADD CONSTRAINT seller_pkey PRIMARY KEY (id);


--
-- TOC entry 4677 (class 2606 OID 16560)
-- Name: users unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT unique_email UNIQUE (email);


--
-- TOC entry 4697 (class 2606 OID 16610)
-- Name: cart user_cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT user_cart_pkey PRIMARY KEY (user_id, product_id);


--
-- TOC entry 4699 (class 2606 OID 16535)
-- Name: users_roles fk_person_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk_person_id FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4700 (class 2606 OID 16540)
-- Name: users_roles fk_role_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- TOC entry 4701 (class 2606 OID 16588)
-- Name: orders orders_consumer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_consumer_fkey FOREIGN KEY (consumer) REFERENCES public.users(id) NOT VALID;


--
-- TOC entry 4703 (class 2606 OID 16583)
-- Name: orders_products orders_products_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 4704 (class 2606 OID 16578)
-- Name: orders_products orders_products_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- TOC entry 4702 (class 2606 OID 16601)
-- Name: orders orders_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_status_fkey FOREIGN KEY (status) REFERENCES public.order_status(id) NOT VALID;


--
-- TOC entry 4698 (class 2606 OID 16485)
-- Name: product seller_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT seller_fkey FOREIGN KEY (seller_id) REFERENCES public.seller(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4705 (class 2606 OID 16616)
-- Name: cart user_cart_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT user_cart_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- TOC entry 4706 (class 2606 OID 16611)
-- Name: cart user_cart_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT user_cart_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2025-10-17 23:47:59

--
-- PostgreSQL database dump complete
--

