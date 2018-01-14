import React from 'react';
import {StyleSheet, TextInput, Button, FlatList, Text, Linking, View, TouchableOpacity, Alert} from 'react-native';
import { StackNavigator } from 'react-navigation';

import * as firebase from 'firebase';

// Initialize Firebase
const firebaseConfig = {
    apiKey: "AIzaSyC7q4RBEPEIDDWsmD9LQYVHKY6DiSrwxJY",
    authDomain: "unijobs-react.firebaseapp.com",
    databaseURL: "https://unijobs-react.firebaseio.com",
    projectId: "unijobs-react",
    storageBucket: "",
    messagingSenderId: "26256313123",
    persistence: true
    };

export const firebaseApp = firebase.initializeApp(firebaseConfig);

class JobDetails extends React.Component {
    constructor(props) {
        super(props);

        console.log(this.props.navigation.state.params);
        this.state = {
            description: this.props.navigation.state.params.job.description,
            city: this.props.navigation.state.params.job.city,
            pay: this.props.navigation.state.params.job.pay
        };
    }

    static navigationOptions = ({navigation}) => ({
        title:  `${navigation.state.params.key}`,
    });

    save(key) {
        const { navigate } = this.props.navigation;
        this.items = firebaseApp.database().ref('jobs');
        this.items.child(key).set(this.state);
        navigate('Home');
    }

    render() {
        return (
            <View>
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    onChangeText={(description) => {
                        this.setState({description: description});
                        }
                    }
                    value={this.state.description}
                />
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    editable = {true}
                    onChangeText={(city) => {
                        this.setState({city});
                    }
                    }
                    value={this.state.city}
                />
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    editable = {true}
                    onChangeText={(pay) => {
                        this.setState({pay});
                    }
                    }
                    value={this.state.pay}
                />
                <Button title="Contact SysAdmin" onPress={() => Linking.openURL('mailto:example@gmail.com')}/>
                <Button title="Save" onPress={() => this.save(this.props.navigation.state.params.key)}/>
            </View>
        )
    }
}

class JobView extends React.Component {
    render(){
        return (
            <TouchableOpacity>
            <View style={styles.jobView}>
            <Text>{this.props.item.job.description}</Text>
            <Text>{this.props.item.job.city}</Text>
            <Text>{this.props.item.job.pay}</Text>
            </View>
            </TouchableOpacity>
        );
    }
}

class AddJob extends React.Component {

    save(){
        const items = firebaseApp.database().ref('jobs');
        items.push({
            description: this.state.description,
            city: this.state.city,
            pay: this.state.pay
        });
    }

    render() {
        const { navigate } = this.props.navigation;
        return (
            <View>
                <View>
                    <TextInput placeholder="Title"
                               onChangeText={(text) => this.setState({description: text})}/>
                    <TextInput placeholder="City"
                               onChangeText={(text) => this.setState({city: text})}/>
                    <TextInput placeholder="Money"
                               onChangeText={(text) => this.setState({pay: text})}/>

                </View>
                <Button onPress={() => {
                    this.save();
                    navigate('Home');
                }
                } title="Add job" style={styles.saveButton}/>
            </View>
        );
}}

class JobList extends React.Component {

    constructor(props){
        super(props);
        this.items = this.getReference().child('jobs');
        this.state = {data: [], user: null};
    }

    static navigationOptions = {
        title: 'View jobs'
    };

    componentDidMount() {
        this.unsubscriber = firebase.auth().onAuthStateChanged((myUser) => {
            if (myUser) {
                this.setState({user: myUser});
            }
        });
    }

    componentWillMount() {
        this.getItems(this.items);
        if (this.unsubscriber) {
            this.unsubscriber();
        }
    }

    getReference() {
        return firebaseApp.database().ref();
    }


    getItems(items) {
        items.on('value', (snap) => {
            var items = [];
            snap.forEach((child) => {
                items.push({
                    job: child.val(),
                    key: child.key
                });
            });
            console.log(items);
            this.setState({
                data: items
            });
        });
    }


    showAlert(title, key) {
        Alert.alert('Confirmation', 'Are you sure you want to delete this job?',
            [
                {
                    title: title,
                    text: 'Yes',
                    onPress: () => {
                        this.deleteJob(key);
                    }
                },
                {text: 'No'}
            ],
            {cancelable: false}
        )
    }

    deleteJob(key) {
        console.log(key);
        this.items.child(key).remove();
    }

    logout() {
        firebase.auth().signOut()
            .then(() => {
                this.setState({user: null});
            });
    }


    render() {
        const { navigate } = this.props.navigation;
        if (!this.state.user) {
            return <Login/>;
        }
        return (
            <View style={styles.container}>
                <Button title="Add new job" onPress={() => navigate('Add')}/>
                <Button title="Sign out" onPress={() => this.logout()}/>
                <FlatList
                    data={this.state.data}
                    extraData={this.state}
                    renderItem={
                        ({item}) =>
                        <View>
                            <JobView item={item}/>
                            <Button
                            onPress={() => navigate('Job', item)}
                            title="See details"
                            />

                            <Button
                                onPress={() => this.showAlert("Are you sure?", item.key)}
                                title="Delete"
                            />
                        </View>
                    }

                />
            </View>
        )
    }
}

class Login extends React.Component {
    state = {email: '', password: '', error: '', loading: false};

    onLoginPress() {
        this.setState({error: '', loading: true});

        const {email, password} = this.state;
        firebase.auth().signInWithEmailAndPassword(email, password)
            .then(() => {
                this.setState({error: '', loading: false});
            })
            .catch(() => {
                //Login was not successful, let's create a new account
                firebase.auth().createUserWithEmailAndPassword(email, password)
                    .then(() => {
                        this.setState({error: '', loading: false});
                    })
                    .catch(() => {
                        this.setState({error: 'Authentication failed.', loading: false});
                    });
            });
    }

    renderButtonOrSpinner() {
        return <Button onPress={this.onLoginPress.bind(this)} title="Log in"/>;
    }

    render() {
        return (
            <View>
                <TextInput
                    label='Email Address'
                    placeholder='you@domain.com'
                    value={this.state.email}
                    onChangeText={email => this.setState({email})}
                />
                <TextInput
                    label='Password'
                    autoCorrect={false}
                    placeholder='*******'
                    secureTextEntry
                    value={this.state.password}
                    onChangeText={password => this.setState({password})}
                />
                <Text style={styles.errorTextStyle}>{this.state.error}</Text>
                {this.renderButtonOrSpinner()}
            </View>
        );
    }
}

const SimpleApp = StackNavigator({
    Home: { screen: JobList },
    Job: { screen: JobDetails },
    Add: { screen: AddJob},
    Login: { screen: Login }
});

export default class App extends React.Component {

  render() {
    return (
    <SimpleApp/>
    );
  }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center'

    },
    jobView: {
        flex:1,
        paddingTop: 30,
        paddingBottom: 30,
        alignItems: 'center',
        backgroundColor: '#e9eaf2',
        borderColor: '#000000',
        borderWidth: 3
    },
    item: {
        padding: 10,
        fontSize: 18,
        height: 44,
    },
    errorTextStyle: {
        color: '#E64A19',
        alignSelf: 'center',
        paddingTop: 10,
        paddingBottom: 10
    },
});
