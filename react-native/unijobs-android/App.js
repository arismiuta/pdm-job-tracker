import React from 'react';
import {StyleSheet, TextInput, Button, FlatList, Text, Linking, View, TouchableOpacity, Alert} from 'react-native';
import { StackNavigator } from 'react-navigation';

const data = [
    {id: 0, key: 'Babysitter', city: 'Cluj', money: '100 RON'},
    {id: 1, key: 'Cleaning', city: 'Timisoara', money: '50 RON'},
    {id: 2, key: 'Grocery shopping', city: 'Deva', money: '20 RON'},
    {id: 3, key: 'Plumbing', city: 'Cluj', money: '120 RON'},
    {id: 4, key: 'Electric work', city: 'Cluj', money: '15 RON'},
    {id: 5, key: 'Redesign bathroom', city: 'Cluj', money: '23 RON'}
];

class JobDetails extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.navigation.state.params.job.id,
            key: this.props.navigation.state.params.job.key,
            city: this.props.navigation.state.params.job.city,
            money: this.props.navigation.state.params.job.money
        }
    }

    static navigationOptions = ({navigation}) => ({
        title:  `${navigation.state.params.job.key}`,
    });


    render() {
        return (
            <View>
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    onChangeText={(key) => {
                        this.setState({key});
                        data[this.state.id]['key'] = key;
                        }
                    }
                    value={this.state.key}
                />
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    editable = {true}
                    onChangeText={(city) => {
                        this.setState({city});
                        data[this.state.id]['city'] = city;
                    }
                    }
                    value={this.state.city}
                />
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1}}
                    editable = {true}
                    onChangeText={(money) => {
                        this.setState({money});
                        data[this.state.id]['money'] = money;
                    }
                    }
                    value={this.state.money}
                />
                <Button title="Contact SysAdmin" onPress={() => Linking.openURL('mailto:example@gmail.com')}/>
            </View>
        )
    }
}

class JobView extends React.Component {
    render(){
        return (
            <TouchableOpacity>
            <View style={styles.jobView}>
            <Text>{this.props.item.key}</Text>
            <Text>{this.props.item.city}</Text>
            <Text>{this.props.item.money}</Text>
            </View>
            </TouchableOpacity>


        );
    }
}

class JobList extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            data: data
        }
    }
    static navigationOptions = {
        title: 'View jobs'
    };


    render() {
        const { navigate } = this.props.navigation;
        return (
            <View style={styles.container}>
                <Button title="Refresh list" onPress={() => this.setState({data: data})}/>
                <FlatList
                    data={this.state.data}
                    extraData={this.state}
                    renderItem={
                        ({item}) =>
                        <View>
                            <JobView item={item}/>
                            <Button
                            onPress={() => navigate('Job', {job: item})}
                            title="See details"
                        />
                        </View>
                    }

                />
            </View>
        )
    }
}

const SimpleApp = StackNavigator({
    Home: { screen: JobList },
    Job: { screen: JobDetails }
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
        backgroundColor: '#2c4d9a',
        borderColor: '#000000',
        borderWidth: 3
    },
    item: {
        padding: 10,
        fontSize: 18,
        height: 44,
    },
});
