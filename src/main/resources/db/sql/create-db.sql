create table imdb.title (
    id int(11) NOT NULL,
    name varchar(255),
    release_timestamp int(11),
    budget int(11),
    primary key (id)
);

create table imdb.genre (
    id int(11) NOT NULL auto_increment,
    name varchar(255),
    primary key (id)
);

create table imdb.title_genre (
    title_id int(11) NOT NULL,
    genre_id int(11) NOT NULL,
    primary key (title_id, genre_id)
);

create table imdb.title_rating (
    title_id int(11) NOT NULL,
    timestamp int(11) NOT NULL,
    rating float NOT NULL,
    primary key (title_id, timestamp)
);